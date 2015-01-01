public class MasterCube {
	static Settings my_settings = new Settings();
		public static void main(String[] args) {
			long i=1, j = 216;
			while (j>0)
			{
				i+=j*(j-1);
				j--;
			}
			OptionsWindow options = new OptionsWindow(my_settings);
			options.main(null);
			while(!my_settings.exit){}
			Window game = new Window("MasterCube", my_settings);
			game.run();
		}
}
