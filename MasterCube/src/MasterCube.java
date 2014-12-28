public class MasterCube {
	static Settings my_settings = new Settings();
		public static void main(String[] args) {
			OptionsWindow options = new OptionsWindow(my_settings);
			options.main(null);
			while(!my_settings.exit){}
			Window game = new Window("MasterCube", my_settings);
			game.run();
		}
}
