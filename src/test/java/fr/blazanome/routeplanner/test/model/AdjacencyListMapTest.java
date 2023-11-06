package fr.blazanome.routeplanner.test.model;

import fr.blazanome.routeplanner.model.AdjacencyListMap;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.model.map.MapBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AdjacencyListMapTest {
    @Test
    void testBuilderValid() {
        AdjacencyListMap.BuilderFactory factory = new AdjacencyListMap.BuilderFactory();
        MapBuilder builder = factory.createBuilder();

        List<Intersection> intersections = Arrays.asList(
                new Intersection(0, 0),
                new Intersection(1, 0)
        );

        for(var intersection: intersections) {
            builder.addIntersection(intersection);
        }

        builder.addSegment(new Segment(intersections.get(0), 5, "Rue des fleurs", intersections.get(1)));

        IMap map = builder.build();

        assertThat(map.getIntersections()).hasSameElementsAs(intersections);
        assertThat(map.getSegments()).hasSameElementsAs(List.of(new Segment(intersections.get(0), 5, "Rue des fleurs", intersections.get(1))));

    }

    @Test
    void testBuilderInvalidOrder() {
        AdjacencyListMap.BuilderFactory factory = new AdjacencyListMap.BuilderFactory();
        MapBuilder builder = factory.createBuilder();

        List<Intersection> intersections = Arrays.asList(
                new Intersection(0, 0),
                new Intersection(1, 0)
        );

        assertThrows(AssertionError.class, ()->builder.addSegment(new Segment(intersections.get(0), 5, "Rue des fleurs", intersections.get(1))));
    }
}
