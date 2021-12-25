package io.ib67;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class UpdateCheckFucker {
    public static UpdateCheckFucker INSTANCE;
    public static void premain(String agentArgs,Instrumentation instrumentation) {
        System.out.println("UpdateCheck Fucker is loading");
        INSTANCE = new UpdateCheckFucker(new File(agentArgs==null?".":agentArgs));
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactoryImpl(INSTANCE::check));
    }
    private List<Pattern> rules= Collections.emptyList();
    private final Mode matchMode;
    private final boolean onlyMainThread = Boolean.parseBoolean(System.getProperty("ucf.onlyMainThread","true"));
    public UpdateCheckFucker(File rootDir){
        // load configurations.
        var confFile = new File(rootDir,System.getProperty("ucf.configFile","rules.txt"));
        if (!confFile.exists()) {
            try (
                    var rulesIn = this.getClass().getClassLoader().getResourceAsStream("rules.txt")
            ){
                Files.write(confFile.toPath(), Objects.requireNonNull(rulesIn).readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            rules = Files.readAllLines(confFile.toPath()).stream().map(Pattern::compile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        matchMode = Mode.valueOf(System.getProperty("ucf.mode","SMART_MATCH"));
    }
    public boolean check(URL u){
        if(onlyMainThread && !Thread.currentThread().getName().equals("main")){
            return true;
        }
        List<StackWalker.StackFrame> frames=null;
        if(matchMode == Mode.SMART_MATCH){
            frames = StackWalker.getInstance().walk(s ->
                    s.dropWhile(f -> f.getClassName().startsWith("org.bukkit."))
                            .limit(10)
                            .collect(Collectors.toList()));
        }
        if(Boolean.getBoolean("ucf.verbose")){
            if(frames==null){
                frames = StackWalker.getInstance().walk(s ->
                        s.dropWhile(f -> f.getClassName().startsWith("org.bukkit."))
                                .limit(10)
                                .collect(Collectors.toList()));
            }
            System.out.println("[UCF Verbose] URL: "+u.toString());
            var a = frames.stream().map(StackWalker.StackFrame::getClassName).collect(joining("\n"));
            System.out.println("[UCF Verbose] Frames: " +a);
            System.out.println("[UCF Verbose] Thread: "+Thread.currentThread().getName());
        }
        switch(matchMode){
            case RULE_MATCH:
                return rules.stream().noneMatch(e->e.matcher(u.toString()).find());
            case SMART_MATCH:
                return frames.stream().noneMatch(e->e.getClass().getSuperclass() == JavaPlugin.class);
            case COMBINED_MATCH:
                return frames.stream().noneMatch(e->e.getClass().getSuperclass() == JavaPlugin.class) && rules.stream().noneMatch(e->e.matcher(u.toString()).find());
        }
        return true;
    }
}
