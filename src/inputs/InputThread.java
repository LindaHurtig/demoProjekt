package inputs;

import java.util.List;

import com.google.common.base.Stopwatch;

import leaderboard.Leaderboard;
import skier.Skier;

public class InputThread implements Runnable {

	private List<Skier> skierList;
	public Thread inputThread;
	private Stopwatch stopWatch;
	private Leaderboard leaderBoard;
	private boolean threadRunning;
	private InputExceptionHandler inputs;

	public InputThread(List<Skier> list, Stopwatch stopWatch, Leaderboard leaderBoard, InputExceptionHandler ieh) {
		this.threadRunning = true;
		this.skierList = list;
		this.stopWatch = stopWatch;
		this.leaderBoard = leaderBoard;
		this.inputs = ieh;
	}

	@Override
	public void run() {
		while(threadRunning) {
			String optionInput = inputs.inputStringNextLine().trim();
			if(optionInput.startsWith("m")) {
				char filterNumber = optionInput.charAt(optionInput.length()-1);				
				Skier selectedSkier = findSkier(filterNumber);
				if(selectedSkier != null) {
					selectedSkier.setSplitTime(selectedSkier.getStartTime() + stopWatch.elapsed().toMillis());
					leaderBoard.printSkierSplitTime(selectedSkier.getStartNumber() - 1);
				}
			} 
			else if(optionInput.startsWith("s")) {
				char filterNumber = optionInput.charAt(optionInput.length()-1);
				Skier selectedSkier = findSkier(filterNumber);
				if(selectedSkier != null) {
					if(selectedSkier.getFinalTime() == -1) {
						selectedSkier.setFinalTime(selectedSkier.getStartTime() + stopWatch.elapsed().toMillis());
						System.out.println("Sparad sluttid '" + selectedSkier.getName() + "': " + leaderBoard.convertTime(selectedSkier.getFinalTime()) + "\n");
					} else {
						System.out.println(selectedSkier.getName() + " har redan �kt i m�l!");
					}
				}
			} 
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopThread() {
		threadRunning = false;
		inputThread.stop();
	}
	
	private Skier findSkier(char filterNumber) {
		int skier = inputs.inputCharToInt(filterNumber, "Du m�ste mata in �karens startnummer efter 'mellantid/sluttid'");
		if(skier == -1) { // ta hand om input felhantering.
			return null;
		}
		else if(skier <= 0 || skier > skierList.size()) { // tar hand om att hitta �karen i skier listan #skierList.
			System.out.println("Kan inte hitta �karen!");
			return null;
		}
		return skierList.get(skier - 1);
	}
}
