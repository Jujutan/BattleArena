package mc.alk.arena.objects.queues;

import java.util.Iterator;

import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.util.Log;


public class CompositeTeamQueue implements  TeamCollection{
	TeamCollection[] queues;
	int size = 0;
	MatchParams mp;

	public CompositeTeamQueue(TeamCollection ... queues) {
		this.queues = queues;
		for (TeamCollection tq: this.queues){
			size += tq.size();
		}
		this.mp = queues[0].getMatchParams();
	}

	public boolean add(QueueObject to) {
		queues[0].add(to);
		size += to.size();
		return true;
	}

	public int size() {
		return size;
	}

	public MatchParams getMatchParams() {
		return mp;
	}

	@Override
	public Iterator<QueueObject> iterator() {
		return new QOIterator(queues);
	}

	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public boolean remove(QueueObject queueObject) {
		for (TeamCollection tq: queues){
			if (tq.remove(queueObject)){
				return true;
			}
		}
		return false;
	}

	class QOIterator implements Iterator<QueueObject>{
		TeamCollection[] queues;
		Iterator<QueueObject> iter;
		int cur = 0;
		public QOIterator(TeamCollection[] queues) {
			this.queues = queues;
			iter = queues[0].iterator();
		}

		@Override
		public boolean hasNext() {
			if (!iter.hasNext()){
				if (++cur >= queues.length){
					return false;
				} else {
					Log.debug("  cur = " + cur +"   " + queues.length);
					iter = queues[cur].iterator();
					return iter.hasNext();
				}
			} else {
				return true;
			}
		}

		@Override
		public QueueObject next() {
			if (iter.hasNext()) {
				return iter.next();
			} else {
				if (++cur >= queues.length){
					return null;
				} else {
					iter = queues[cur].iterator();
					return iter.next();
				}
			}
		}

		@Override
		public void remove() {
			iter.remove();
		}
	}
}