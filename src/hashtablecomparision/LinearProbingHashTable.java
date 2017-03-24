/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtablecomparision;

/**
 *
 * @author param
 */

import java.util.ArrayList;



public class LinearProbingHashTable<K, V> implements HashTable<K, V> {
	private PairB<K, V> map[];
	private int mapLength;
	private int size;
	public int collision;

	@SuppressWarnings("unchecked")
	public LinearProbingHashTable() {
		mapLength = 100;
		size = 0;
		map = new PairB[mapLength];
	}

	@SuppressWarnings("unchecked")
	private LinearProbingHashTable(int mapLength) {
		this.mapLength = mapLength;
		size = 0;
		map = new PairB[mapLength];
		collision = 0;
	}

	private void rehash() {
		Iterable<K> keys = keys();
		LinearProbingHashTable<K, V> temp = new LinearProbingHashTable<K, V>(
				mapLength * 2);
		for (K key : keys) {
			V value = get(key);
			temp.put(key, value);
		}
		map = temp.map;
		size = temp.size;
		mapLength = temp.mapLength;
		// System.out.println("Rehashing : Size = " + size + " mapLength = "
		// + mapLength);
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
		int home = getHashCode(pair);
		for (int i = 0; i < mapLength; i++) {
			int index = getNextProbe(home, i);
			if (map[index] == null) {
				break;
			} else if (map[index].equals(pair)) {
				map[index] = pair;
				return;
			}
		}
		for (int i = 0; i < mapLength; i++) {
			int index = getNextProbe(home, i);
			if (map[index] == null || map[index].isDeleted()) {
				map[index] = pair;
				size++;
				return;
			} else {
				collision++;
			}
		}
	}

	// get value paired with key, return null if
	// hash don't contain key

	@Override
	public V get(K key) {
		PairB<K, V> pair = new PairB<K, V>(key, null);
		int home = getHashCode(pair);
		for (int i = 0; i < mapLength; i++) {
			int index = getNextProbe(home, i);
			if (map[index] == null) {
				return null;
			} else if (map[index].equals(pair) && !map[index].isDeleted()) {
				return map[index].getValue();
			}
		}
		return null;
	}

	// remove key (and its value) from table

	@Override
	public void delete(K key) {
		PairB<K, V> pair = new PairB<K, V>(key, null);
		int home = getHashCode(pair);
		for (int i = 0; i < mapLength; i++) {
			int index = getNextProbe(home, i);
			if (map[index] == null) {
				return;
			} else if (map[index].equals(pair) && !map[index].isDeleted()) {
				map[index].setDeleted(true);
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
		int home = getHashCode(pair);
		for (int i = 0; i < mapLength; i++) {
			int index = getNextProbe(home, i);
			if (map[index] == null) {
				return false;
			} else if (map[index].equals(pair)) {
				return !map[index].isDeleted();
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
		for (int i = 0; i < mapLength; i++) {
			if (map[i] != null && !map[i].isDeleted()) {
				keys.add(map[i].getKey());
			}
		}
		return keys;
	}

	private int getHashCode(PairB<K, V> pair) {
		return pair.hashCode() % mapLength;
	}

	private int getNextProbe(int home, int i) {
		return (home + i) % mapLength;
	}

	@Override
	public String toString() {
		return "\nNumber of Collsion = " + collision;
	}
}
