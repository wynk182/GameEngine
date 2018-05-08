package application;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DamageTask extends Service {
	InfoBox d;
	int time;

	public DamageTask(InfoBox d, int time) {
		this.d = d;
		this.time = time;
	}

	@Override
	protected Task createTask() {
		return new Task<Void>() {           
            @Override
            protected Void call() throws Exception {
            	try {
                   	Thread.sleep(time *1000);
                   	Platform.runLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							d.hideInfo();

						}
                   		
                   	});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return null;
            }
        };
}

}
