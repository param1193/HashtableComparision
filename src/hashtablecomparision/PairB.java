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

public class PairB<K, V> {
	private K key;
	private V value;
	private boolean deleted;

	public PairB(K key, V value) {
		this.key = key;
		this.value = value;
		deleted = false;
	}

	@Override
	public int hashCode() {
		return Math.abs(key.hashCode());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		PairB<K, V> pair = (PairB<K, V>) o;
		return key.equals(pair.getKey());
	}

	@Override
	public String toString() {
		return key + " = " + value;
	}

	// ---------------------------------------------

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
