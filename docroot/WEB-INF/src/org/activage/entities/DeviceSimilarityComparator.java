package org.activage.entities;

import java.util.Comparator;

public class DeviceSimilarityComparator implements Comparator<Device>{

	@Override
	public int compare(Device o1, Device o2) {
		return Double.compare(o2.getSimilarityValue(), o1.getSimilarityValue());
	}

}
