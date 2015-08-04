package tei.kav.smartorder;

public class AliveHolder {
	private static int count;
	public static void add(){
		count++;
	}
	public static void remove(){
		count--;
		if (count==0){
			new MyHttpClient().execute("post","UserOffline");
		}
	}
}
