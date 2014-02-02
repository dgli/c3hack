package lanex.c3.midi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import lanex.c3.C3App;
import lanex.c3.C3Game;
import lanex.c3.C3Gameover;


public class ScrollingMusicSheet {
//	public static int ticksPerPixel
	public float pixelsPerTick = 0.5f;
	public Note currentActiveNote;
	public Channel sourceChannel;
	public LinkedList<Note> activeNotes;
	public int currentTick = -999;
	public boolean done;
	
	public ScrollingMusicSheet(Channel src){
		sourceChannel = src;
		activeNotes = new LinkedList<Note>();
		//notes = sourceTrack.getNotes();
	}
	
	void renderList(){
		Iterator<Note> activeListIterator = activeNotes.iterator();
		
		while(activeListIterator.hasNext()){
			Note temp = activeListIterator.next();
			
			if(temp.getStopTick() < currentTick-getOffscreenTickDelta()){
				//System.out.println("Deleting Note with end time: " + temp.getStopTick());
				activeListIterator.remove();
			}
			
			
		}
		
		Iterator<Note> sourceListIterator = ((Queue)((LinkedList)(sourceChannel.getNotes())).clone()).iterator();
		
		while(sourceListIterator.hasNext()){
			Note temp = sourceListIterator.next();
			if(temp.getStartTick() > currentTick+getOffscreenTickDelta()){
				break;
			}
			//System.out.println("Adding Note with start time: " + temp.getStartTick());
			sourceListIterator.remove();
			
			int frames = (short)((temp.getStopTick() - temp.getStartTick())*C3Game.testMap.getMillisPerTick()*60/1000);
			temp.collisionHistory = new short[frames];
					
			activeNotes.push(temp);
		}
		if (!sourceListIterator.hasNext())
			done = true;
		
	}
	
	public int getOffscreenTickDelta(){
		return (int) (C3App.RENDER_WIDTH*1.0/2/pixelsPerTick);
	}
	
	void findNewCurrentPitch(){
		if(currentActiveNote == null || currentActiveNote.getStopTick() < currentTick){

			Iterator<Note> activeListIterator = activeNotes.iterator();
			
			while(activeListIterator.hasNext()){
				Note temp = activeListIterator.next();
				
				if(temp.getStartTick() < currentTick){
					currentActiveNote = temp;
					//System.out.println("new current note.");
					return;
				}
			}
			
			currentActiveNote = null;
		}
	}
	
	public void update(int currentTick, float pitchDifference){
		this.currentTick = currentTick;
		
		renderList();
		
		findNewCurrentPitch();
		
		if(currentActiveNote != null && currentActiveNote.collisionHistory != null){
			
			if(Math.abs(pitchDifference) > 0.5){
				return;
			}
			
			//int frames = (short)((currentActiveNote.getStopTick() - currentActiveNote.getStartTick())*C3Game.testMap.getMillisPerTick()*60/1000);
			int nowFrame = (short)((currentTick - currentActiveNote.getStartTick())*C3Game.testMap.getMillisPerTick()*60/1000);
			if(nowFrame < currentActiveNote.collisionHistory.length){
				currentActiveNote.collisionHistory[nowFrame] = (short)(Math.abs(0.5 - pitchDifference) * 300);
				C3Gameover.score += currentActiveNote.collisionHistory[nowFrame];
			}
		}
		
	}
}
