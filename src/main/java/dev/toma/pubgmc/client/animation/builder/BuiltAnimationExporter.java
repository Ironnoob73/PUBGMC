package dev.toma.pubgmc.client.animation.builder;

import com.google.gson.JsonParseException;
import dev.toma.pubgmc.util.object.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BuiltAnimationExporter {

    public static void exportAnimation() {
        LocalDateTime time = LocalDateTime.now();
        String fileName = "animation_" + normal(time.getDayOfMonth()) + normal(time.getMonthValue()) + time.getYear() + "_" + normal(time.getHour()) + normal(time.getMinute());
        File file = findSuitableNameFor(fileName);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(export());
            writer.close();
            StringTextComponent textComponent = new StringTextComponent("File has been created, path: " + file.getCanonicalPath());
            textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getParentFile().getCanonicalPath()));
            Minecraft.getInstance().player.sendMessage(textComponent);
        } catch (IOException e) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.RED + "Could not create file: " + e.toString()));
            e.printStackTrace();
        } catch (JsonParseException e) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.RED + "Error serializing animation: " + e.toString()));
            e.printStackTrace();
        } catch (Exception e) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.RED + "Unknown error: " + e.toString()));
            e.printStackTrace();
        }
    }

    private static String export() {
        DecimalFormat df = new DecimalFormat("###.##");
        StringBuilder builder = new StringBuilder();
        float first = 0.0F;
        for(BuilderAnimationStep step : BuilderData.steps) {
            float last = first + step.length;
            builder.append("addStep(").append(df.format(first)).append("F, ").append(last).append("F, SimpleAnimation.newSimpleAnimation()");
            first = last;
            for(BuilderData.Part part : BuilderData.Part.values()) {
                BuilderAnimationStep.Data data = step.map.get(part);
                if(data.isEmpty()) continue;
                builder.append(getFunctionName(part)).append(generateAnimation(data)).append("})");
            }
            builder.append(".create());\n");
        }
        return builder.toString();
    }

    private static String generateAnimation(BuilderAnimationStep.Data data) {
        DecimalFormat df = new DecimalFormat("###.##");
        StringBuilder builder = new StringBuilder();
        BuilderAnimationStep.TranslationContext tctx = data.translationContext;
        if(!tctx.isEmpty()) {
            builder.append("GlStateManager.translate(");
            handleTranslationValue(builder, df, tctx.baseX, tctx.newX, true);
            handleTranslationValue(builder, df, tctx.baseY, tctx.newY, true);
            handleTranslationValue(builder, df, tctx.baseZ, tctx.newZ, false);
            builder.append(");");
        }
        BuilderAnimationStep.RotationContext ctx = data.rotationContext;
        if(!ctx.isEmpty()) {
            for(Map.Entry<BuilderData.Axis, Pair<Float, Float>> entry : ctx.rotations.entrySet()) {
                builder.append("GlStateManager.rotate(");
                Pair<Float, Float> sdP = entry.getValue();
                handleTranslationValue(builder, df, sdP.getLeft(), sdP.getRight(), true);
                switch (entry.getKey()) {
                    case X: builder.append("1.0F, 0.0F, 0.0F);"); break;
                    case Y: builder.append("0.0F, 1.0F, 0.0F);"); break;
                    case Z: builder.append("0.0F, 0.0F, 1.0F);"); break;
                }
            }
        }
        return builder.toString();
    }

    private static void handleTranslationValue(StringBuilder builder, DecimalFormat format, float s, float d, boolean addComma) {
        boolean flag = false;
        if(s != 0) {
            builder.append(format.format(s)).append("F");
            flag = true;
        }
        if(d != 0) {
            flag = true;
            if(d < 0) builder.append(format.format(d)).append("F");
            else builder.append("+").append(format.format(d)).append("F");
            builder.append("*f");
        }
        if(!flag) builder.append("0.0F");
        if(addComma) builder.append(", ");
    }

    private static String getFunctionName(BuilderData.Part part) {
        switch (part) {
            case ITEM: return ".item(f -> {";
            case ITEM_AND_HANDS: default: return ".itemHand(f -> {";
            case LEFT_HAND: return ".leftHand(f -> {";
            case RIGHT_HAND: return ".rightHand(f -> {";
            case HANDS: return ".hands(f -> {";
        }
    }

    private static String normal(int input) {
        if(input < 10) {
            return "0" + input;
        } else return input + "";
    }

    private static File findSuitableNameFor(String name) {
        File export = new File(Minecraft.getInstance().gameDir, "export");
        if(!export.exists()) {
            export.mkdirs();
        }
        File file = new File(export, name + ".txt");
        if(!file.exists()) {
            return file;
        } else {
            int added = 0;
            while (true) {
                String newName = name + "_" + added;
                File ff = new File(export, newName + ".txt");
                if(!ff.exists()) {
                    return ff;
                }
                added++;
            }
        }
    }
}
