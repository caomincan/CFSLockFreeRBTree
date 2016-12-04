package queue;

public class work implements Comparable<work> {
    int key;
    
    public work(int key){
    	this.key = key;
    }
	@Override
	public int compareTo(work o) {
		// TODO Auto-generated method stub
		return this.key-o.key;
	}

}
