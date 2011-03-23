@Configuration
public class SimpleSampleCommandConfig extends AbstractCommandConfig {

    @Bean
    @Qualifier("menubar")
    public CommandGroup menuBarCommandGroup() {
        CommandGroupFactoryBean menuFactory = new CommandGroupFactoryBean();
        menuFactory.setGroupId("menu");
        return menuFactory.getCommandGroup();
    }

    @Bean
    @Qualifier("toolbar")
    public CommandGroup toolBarCommandGroup() {
        CommandGroupFactoryBean toolbarFactory = new CommandGroupFactoryBean();
        toolbarFactory.setGroupId("toolbar");
        return toolbarFactory.getCommandGroup();
    }
}
