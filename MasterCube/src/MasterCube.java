public class MasterCube {
	static Settings my_settings = new Settings();
		public static void main(String[] args) {
			long i=1, j = 62, k=64;
			i=j;
			while (j>0)
			{
				j--;
				k+=i*j;
				i=i*j;
			}
			OptionsWindow options = new OptionsWindow(my_settings);
			options.main(null);
			while(!my_settings.exit){}
			Window game = new Window("MasterCube", my_settings);
			game.run();
		}
}
