/* 
 * Convex hull algorithm - Library (Java)
 * 
 * Copyright (c) 2017 Project Nayuki
 * https://www.nayuki.io/page/convex-hull-algorithm
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (see COPYING.txt and COPYING.LESSER.txt).
 * If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class ConvexHull {
	
	// Returns a new list of points representing the convex hull of
	// the given set of points. The convex hull excludes collinear points.
	// This algorithm runs in O(n log n) time.
	public static List<Point> makeHull(List<Point> points) {
		List<Point> newPoints = new ArrayList<>(points);
		Collections.sort(newPoints);
		return makeHullPresorted(newPoints);
	}
	
	
	// Returns the convex hull, assuming that each points[i] <= points[i + 1]. Runs in O(n) time.
	public static List<Point> makeHullPresorted(List<Point> points) {
		if (points.size() <= 1)
			return new ArrayList<>(points);
		
		// Andrew's monotone chain algorithm. Positive y coordinates correspond to "up"
		// as per the mathematical convention, instead of "down" as per the computer
		// graphics convention. This doesn't affect the correctness of the result.
		
		List<Point> upperHull = new ArrayList<>();
		int i = 0;
		while (i + 1 < points.size() && points.get(i + 1).x == points.get(i).x)
			i++;
		upperHull.add(points.get(i));
		
		for (i++; i < points.size(); i++) {
			Point p = points.get(i);
			while (upperHull.size() >= 2) {
				Point q = upperHull.get(upperHull.size() - 1);
				Point r = upperHull.get(upperHull.size() - 2);
				double tangent = (p.y - r.y) / (p.x - r.x);
				double prevTangent = (q.y - r.y) / (q.x - r.x);
				if (tangent < prevTangent)
					break;
				else
					upperHull.remove(upperHull.size() - 1);
			}
			upperHull.add(p);
		}
		
		List<Point> lowerHull = new ArrayList<>();
		i = points.size() - 1;
		while (i > 0 && points.get(i - 1).x == points.get(i).x)
			i--;
		lowerHull.add(points.get(i));
		
		for (i--; i >= 0; i--) {
			Point p = points.get(i);
			while (lowerHull.size() >= 2) {
				Point q = lowerHull.get(lowerHull.size() - 1);
				Point r = lowerHull.get(lowerHull.size() - 2);
				double tangent = (p.y - r.y) / (p.x - r.x);
				double prevTangent = (q.y - r.y) / (q.x - r.x);
				if (tangent < prevTangent)
					break;
				else
					lowerHull.remove(lowerHull.size() - 1);
			}
			lowerHull.add(p);
		}
		
		if (lowerHull.get(lowerHull.size() - 1).equals(upperHull.get(0)))
			lowerHull.remove(lowerHull.size() - 1);
		if (!lowerHull.isEmpty() && lowerHull.get(0).equals(upperHull.get(upperHull.size() - 1)))
			lowerHull.remove(0);
		upperHull.addAll(lowerHull);
		return upperHull;
	}
	
}



final class Point implements Comparable<Point> {
	
	public final double x;
	public final double y;
	
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	public String toString() {
		return String.format("Point(%g, %g)", x, y);
	}
	
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		else {
			Point other = (Point)obj;
			return x == other.x && y == other.y;
		}
	}
	
	
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	
	public int compareTo(Point other) {
		if (x != other.x)
			return Double.compare(x, other.x);
		else
			return Double.compare(y, other.y);
	}
	
}
