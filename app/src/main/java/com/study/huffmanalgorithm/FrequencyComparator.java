package com.study.huffmanalgorithm;

import java.util.Comparator;

public class FrequencyComparator implements Comparator<Node>  {
    //빈도수
        //빈도수 낮은 것이 우선순위가 높다
        public int compare(Node a, Node b) {
            int frequencyA = a.frequency;
            int frequencyB = b.frequency;
            return frequencyA - frequencyB;
        }
}
