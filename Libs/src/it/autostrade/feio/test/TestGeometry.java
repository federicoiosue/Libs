package it.autostrade.feio.test;

import java.util.ArrayList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;


public class TestGeometry {

	public static void main(String[] args) {

		final GeometryFactory gf = new GeometryFactory();

		// PROVA SITO 261
		 double longitudine =  44.40748;
		 double latitudine = 12.09386;
		 double v1lat =  44.4203;
		 double v1lon =  12.012;
		 double v2lat =  44.4188;
		 double v2lon =  12.099;
		 double v3lat =  44.3563;
		 double v3lon =  12.097;
		 double v4lat =  44.3578;
		 double v4lon =  12.01;

		// PROVA SITO 240
//		double longitudine = 42.471630;
//		double latitudine = 14.11605;
//		double v1lat = 42.4898;
//		double v1lon = 14.069;
//		double v2lat = 42.4868;
//		double v2lon = 14.154;
//		double v3lat = 42.4244;
//		double v3lon = 14.15;
//		double v4lat = 42.4274;
//		double v4lon = 14.065;


		// Definisco il punto
		final Point point = gf.createPoint(new Coordinate(longitudine, latitudine));

		// Inserisco le coordinate del quadrante
		final ArrayList<Coordinate> points = new ArrayList<Coordinate>();
		points.add(new Coordinate(v1lat, v1lon));
		points.add(new Coordinate(v2lat, v2lon));
		points.add(new Coordinate(v3lat, v3lon));
		points.add(new Coordinate(v4lat, v4lon));
		points.add(new Coordinate(v1lat, v1lon));
		// Creo il poligono
		final Polygon polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points.toArray(new Coordinate[points.size()])), gf), null);

		System.out.println(point.within(polygon));
	}
}
