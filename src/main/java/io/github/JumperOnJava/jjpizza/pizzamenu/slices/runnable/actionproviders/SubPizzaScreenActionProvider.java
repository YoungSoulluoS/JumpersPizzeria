package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaScreen;
import io.github.JumperOnJava.jjpizza.pizzamenu.SubPizzaManager;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.AskScreenManager;
import io.github.JumperOnJava.lavajumper.gui.OverlayEmptyAskScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Random;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;

public class SubPizzaScreenActionProvider implements ConfigurableRunnable {
    private String id = "";
    private transient SubPizzaManager pizza;

    public SubPizzaScreenActionProvider(Boolean aBoolean) {
    }

    @Override
    public Screen getConfiguratorScreen() {
        return new SubPizzaEditScreen(this);
    }

    @Override
    public ConfigurableRunnable copy() {
        var sp = new SubPizzaScreenActionProvider(true);
        sp.id=id;
        return sp;
    }

    @Override
    public void run() {
        makeSurePizzaExists();
        pizza.openPizza(MinecraftClient.getInstance());
    }

    private void makeSurePizzaExists() {
        if(id.equals("")){
            id = "Randomname-" + new Random().nextInt(0,0xFFFF);
        }
        if(pizza == null || !id.equals(pizza.id)){
            pizza = new SubPizzaManager(id);
        }
    }

    private static class SubPizzaEditScreen extends Screen {
        private final SubPizzaScreenActionProvider target;
        private ScrollListWidget list;

        protected SubPizzaEditScreen(SubPizzaScreenActionProvider target) {
            super(Text.empty());
            this.target = target;
        }


        public void init() {
            var nameBox = new TextFieldWidget(client.textRenderer, gap/2, gap/2, width/2-gap, 20, Text.empty());
            nameBox.setText(target.id);
            nameBox.setChangedListener(this::updatePizzaId);
            addDrawableChild(nameBox);

            var button = new ButtonWidget.Builder(Translation.get("jjpizza.subpizza.edit"),this::editSelectedPizza)
                    .dimensions(width/2+gap/2,gap/2,width/2-gap,20)
                    .build();
            addDrawableChild(button);
            this.list = new ScrollListWidget(client,width-gap,height-20-gap,gap/2,20+gap/2,20);
            addDrawableChild(list);
            updateList();
            new SubPizzaManager("lmaohack");//we create new subpizza to ensure directory exists :concern:
        }

        private void updateList() {
            var files = FabricLoader.getInstance().getConfigDir().resolve("jjpizza/sub").toFile().listFiles();

            for(var file : files){
                var filename = file.getName().replace(".json","");
                var entry = new ScrollListWidget.ScrollListEntry();
                entry.addDrawableChild(new ButtonWidget.Builder(Text.literal(filename),(b)->{
                    entry.setMeActive();
                    target.id = filename;
                }).dimensions(gap/2,gap/2,width-40-6*gap/2*2,0).build(),true);
                entry.addDrawableChild(new ButtonWidget.Builder(Text.literal("X"),b->{
                    file.delete();
                    updateList();
                }).dimensions(width-40-6+gap/2,0,20,20).build(),false);
                entry.addDrawableChild(new ButtonWidget.Builder(Text.literal("E"),b->{
                    AskScreenManager.ask(new OverlayEmptyAskScreen(new SubPizzaManager(filename).getBuilderScreen()));
                }).dimensions(width-20-6+gap/2,0,20,20).build(),false);
                list.addEntry(entry);
            }
        }

        private void select(ButtonWidget buttonWidget) {

        }

        private void editSelectedPizza(ButtonWidget buttonWidget) {
            target.makeSurePizzaExists();
            AskScreenManager.ask(new OverlayEmptyAskScreen(target.pizza.getBuilderScreen()));
        }

        private void updatePizzaId(String s) {
            target.id = s;
        }


    }
}
