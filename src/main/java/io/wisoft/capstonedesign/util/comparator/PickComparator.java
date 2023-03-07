package io.wisoft.capstonedesign.util.comparator;

import io.wisoft.capstonedesign.domain.Pick;

import java.util.Comparator;

public class PickComparator implements Comparator<Pick> {
    @Override
    public int compare(Pick pick1, Pick pick2) {
        return pick1.getPickedAt().compareTo(pick2.getPickedAt());
    }
}
