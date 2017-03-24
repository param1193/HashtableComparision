package hashtablecomparision;

/**
 *
 * @author param
 */


import java.util.ArrayList;



public class BucketingHashTable<K, V> implements HashTable<K, V> {
	private PairB<K, V> map[];
	private ArrayList<PairB<K, V>> overflow;
	private int[] buckets;
	private int mapLength;
	private int size;
	private int bucketSize;
	private int numOfBuckets;
	public int collision;

	@SuppressWarnings("unchecked")
	public BucketingHashTable() {
		mapLength = 50;
		size = 0;
		map = new PairB[mapLength];
		overflow = new ArrayList<PairB<K, V>>();
		bucketSize = 5;
		numOfBuckets = mapLength / bucketSize;
		buckets = new int[numOfBuckets];
		collision = 0;
	}

	@SuppressWarnings("unchecked")
	private BucketingHashTable(int mapLength) {
		this.mapLength = mapLength;
		size = 0;
		map = new PairB[mapLength];
		overflow = new ArrayList<PairB<K, V>>();
		bucketSize = 5;
		numOfBuckets = mapLength / bucketSize;
		buckets = new int[numOfBuckets];
	}

	private void rehash() {
		Iterable<K> keys = keys();
		BucketingHashTable<K, V> temp = new BucketingHashTable<K, V>(
				mapLength * 2);
		for (K key : keys) {
			V value = get(key);
			temp.put(key, value);
		}
		map = temp.map;
		overflow = temp.overflow;
		buckets = temp.buckets;
		mapLength = temp.mapLength;
		size = temp.size;
		bucketSize = temp.bucketSize;
		numOfBuckets = temp.numOfBuckets;
//		System.out.println("Rehashing : Size = " + size + " mapLength = "
//				+ mapLength);
		// keys = keys();
	}

	// put key­value pair into the table

	@Override
	public void put(K key, V value) {
		double loadFactor = (double) size / (double) mapLength;
		if (loadFactor >= .75) {
			rehash();
		}
		PairB<K, V> pair = new PairB<K, V>(key, value);
		int mapIndex = getHashCode(pair);
		if (buckets[mapIndex] == bucketSize) {
			for (int i = mapIndex * bucketSize; i < mapIndex * bucketSize
					+ buckets[mapIndex]; i++) {
				if (map[i].equals(pair)) {
					map[i].setValue(value);
					return;
				}
			}
			if (overflow.contains(pair)) {
				int index = overflow.indexOf(pair);
				overflow.set(index, pair);
			} else {
				for (int i = mapIndex * bucketSize; i < mapIndex * bucketSize
						+ buckets[mapIndex]; i++) {
					if (map[i] == null || map[i].isDeleted()) {
						map[i] = pair;
						size++;
						return;
					} else {
						collision++;
					}
				}
				overflow.add(pair);
				size++;
			}
		} else {
			for (int i = mapIndex * bucketSize; i < mapIndex * bucketSize
					+ buckets[mapIndex]; i++) {
				if (map[i].equals(pair)) {
					map[i].setValue(value);
					return;
				}
			}
			for (int i = mapIndex * bucketSize; i < mapIndex * bucketSize
					+ buckets[mapIndex]; i++) {
				if (map[i].isDeleted()) {
					map[i] = pair;
					return;
				} else {
					collision++;
				}
			}
			map[mapIndex * bucketSize + buckets[mapIndex]] = pair;
			buckets[mapIndex]++;
			size++;
		}
	}

	// get value paired with key, return null if
	// hash don't contain key

	@Override
	public V get(K key) {
		PairB<K, V> pair = new PairB<K, V>(key, null);
		int index = getHashCode(pair);
		if (buckets[index] != bucketSize) {
			for (int i = index * bucketSize; i < index * bucketSize
					+ buckets[index]; i++) {
				if (map[i].equals(pair) && !map[i].isDeleted()) {
					return map[i].getValue();
				}
			}
			return null;
		} else {
			for (int i = index * bucketSize; i < index * bucketSize
					+ buckets[index]; i++) {
				if (map[i].equals(pair) && !map[i].isDeleted()) {
					return map[i].getValue();
				}
			}
			if (overflow.contains(pair)) {
				index = overflow.indexOf(pair);
				if (!overflow.get(index).isDeleted()) {
					return overflow.get(index).getValue();
				}
			}
			return null;
		}
	}

	// remove key (and its value) from table

	@Override
	public void delete(K key) {
		PairB<K, V> pair = new PairB<K, V>(key, null);
		int index = getHashCode(pair);
		if (buckets[index] != bucketSize) {
			for (int i = index * bucketSize; i < index * bucketSize
					+ buckets[index]; i++) {
				if (map[i].equals(pair) && !map[i].isDeleted()) {
					map[i].setDeleted(true);
					size--;
					return;
				}
			}

		} else {
			for (int i = index * bucketSize; i < index * bucketSize
					+ buckets[index]; i++) {
				if (map[i].equals(pair) && !map[i].isDeleted()) {
					map[i].setDeleted(true);
					size--;
					return;
				}
			}
			if (overflow.contains(pair)) {
				overflow.remove(pair);
				size--;
				return;
			}
		}
	}

	// return true if there is a value paired with key
	// and false otherwise.

	@Override
	public boolean contains(K key) {
		PairB<K, V> pair = new PairB<K, V>(key, null);
		int index = getHashCode(pair);
		if (buckets[index] != bucketSize) {
			for (int i = index * bucketSize; i < index * bucketSize
					+ buckets[index]; i++) {

				if (map[i].equals(pair) && !map[i].isDeleted()) {
					return true;
				}

			}

		} else {
			for (int i = index * bucketSize; i < index * bucketSize
					+ buckets[index]; i++) {
				if (map[i].equals(pair) && !map[i].isDeleted()) {
					return true;
				}
			}
			if (overflow.contains(pair)) {
				index = overflow.indexOf(pair);
				return !overflow.get(index).isDeleted();
			}
		}
		return false;
	}

	// return true if the table is empty.

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	// return size of the table.

	@Override
	public int size() {
		return size;
	}

	// all keys in the table

	@Override
	public Iterable<K> keys() {
		ArrayList<K> keys = new ArrayList<K>();
		for (int i = 0; i < map.length; i++) {
			if (map[i] != null && !map[i].isDeleted()) {
				keys.add(map[i].getKey());
			}
		}

		for (int i = 0; i < overflow.size(); i++) {
			if (!overflow.get(i).isDeleted()) {
				keys.add(overflow.get(i).getKey());
			}
		}
		return keys;
	}

	private int getHashCode(PairB<K, V> pair) {
		return pair.hashCode() % numOfBuckets;
	}

	@Override
	public String toString() {
		return "\nNumber of Collsion = " + collision;
	}
}
