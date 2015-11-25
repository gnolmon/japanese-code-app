package com.ana.DrawCharacter;


import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;
import android.util.Log;

public class StrokePath {
	private List<PointF> points;

	private boolean flag_data_changed;
	private float longOfStroke;
	float[] longOfParts;
	float[][] vectorOfPaths;
	Direction[] vectorDirection;

	public StrokePath() {
		this.points = new ArrayList<PointF>();
		flag_data_changed = true;
		longOfStroke = 0;
	}

	public float getLongPath() {
		if (flag_data_changed && points.size() > 1) {
			longOfStroke = 0;
			longOfParts = new float[points.size() - 1];
			vectorOfPaths = new float[points.size() - 1][2];
			for (int i = 1; i < points.size(); i++) {
				float deltaX = points.get(i).x - points.get(i - 1).x, deltaY = points
						.get(i).y - points.get(i - 1).y;
				vectorOfPaths[i - 1][0] = deltaX;
				vectorOfPaths[i - 1][1] = deltaY;
				longOfParts[i - 1] = (float) Math.sqrt(deltaX * deltaX + deltaY
						* deltaY);
				longOfStroke += longOfParts[i - 1];
			}
			flag_data_changed = false;
		}
		return longOfStroke;
	}

	public Direction[] getDirectionVectors() {
		getLongPath();
		if (vectorOfPaths != null) {
			if (vectorDirection == null
					|| vectorDirection.length != vectorOfPaths.length) {
				vectorDirection = new Direction[vectorOfPaths.length];
				for (int i = 0; i < vectorDirection.length; i++) {
					vectorDirection[i] = Direction.get(vectorOfPaths[i][0],
							vectorOfPaths[i][1]);

				}
			}
			return vectorDirection;
		}
		return null;
	}

	public List<Direction> getOrderDirection() {

		Direction[] directions = this.getDirectionVectors();
		if (directions != null) {
			List<Direction> orderDir = new ArrayList<Direction>();
			String buff = "";
			String buff2 = "";
			Direction tmp = null;
			for (Direction direction : directions) {
				buff += " " + direction.toString();
				if (tmp == null || !tmp.equals(direction)) {
					tmp = direction;
					orderDir.add(tmp);
				}
			}
			for (Direction direction : orderDir) {
				buff2 += " " + direction.toString();
			}
			Log.d("MYAPP", buff);
			Log.d("MYAPP", buff2);
			return orderDir;
		}
		return null;
	}

public StrokePath getReduceStroke(float perOfReduce) {
			if (points == null || points.size() < 1) {
				return null;
			}
			float longpath = getLongPath();
	//		float lpath = longpath * perOfReduce;
			float lpath = 40;
			float lget = lpath;
	
			StrokePath result = new StrokePath();
			result.add(points.get(0).x, points.get(0).y);
	
			Direction tmp = Direction.get(vectorOfPaths[0][0], vectorOfPaths[0][1]);
			Direction nextdir = null;
			for (int i = 0; i < vectorOfPaths.length; i++) {
				if (i < vectorOfPaths.length - 1) {
					nextdir = Direction.get(vectorOfPaths[i + 1][0],
							vectorOfPaths[i + 1][1]);
					if (!nextdir.isNear(tmp)) {
						result.add(points.get(i + 1).x, points.get(i + 1).y);
						tmp = nextdir;
						continue;
					}
				}
				PointF start = points.get(i);
				float lremain = longOfParts[i];
	
				int k = 0;
	
				while (lremain >= lget) {
					k++;
					float x = start.x + k * lget / longOfParts[i]
							* vectorOfPaths[i][0];
					float y = start.y + k * lget / longOfParts[i]
							* vectorOfPaths[i][1];
					result.add(x, y);
					lremain -= lget;
					lget = lpath;
				}
				lget = lget - lremain;
	
			}
	//		result.add(points.get(points.size() - 1).x,
	//				points.get(points.size() - 1).y);
			return result;
		}

public PointF get(int location) {
		return points.get(location);
	}

public void add(float x, float y) {
		points.add(new PointF(x, y));
		flag_data_changed = true;
	}

	public int size() {
	return points.size();
}

	//	public boolean compare(StrokePath other) {
//		List<Direction> listorderthis = this.getOrderDirection();
//		List<Direction> listorderother = other.getOrderDirection();
//		if (listorderthis.size() == listorderother.size()) {
//			for (int i = 0; i < listorderthis.size(); i++) {
//				if (!listorderthis.get(i).equals(listorderother.get(i))) {
//					return false;
//				}
//			}
//			return true;
//		}
//
//		return false;
//	}
	public enum Direction {
		/** Basically N */
		NW(0, 0, "\u2196", "NW"), N(0, 1, "\u2191", "N"), NE(0, 2, "\u2197",
				"NE"), W(1, 0, "\u2190", "W"), X(1, 1, "\u26aa", "X"), E(1, 2,
				"\u2192", "E"), SW(2, 0, "\u2199", "SW"), S(2, 1, "\u2193", "S"), SE(
				2, 2, "\u2198", "SE");

		private int indexx;
		private int indexy;
		private String display;
		private String notion;

		Direction(int indexx, int indexy, String display, String notion) {
			this.indexx = indexx;
			this.indexy = indexy;
			this.display = display;
			this.notion = notion;
		}

		@Override
		public String toString() {
			return notion;
		}

		public boolean isNear(Direction other) {
			if (Math.abs(this.indexx - other.indexx) > 1) {
				return false;
			}
			if (Math.abs(this.indexy - other.indexy) > 1) {
				return false;
			}
			return true;
		}

		private static float DIAGONAL_THRESHOLD_FLOAT = 0.40F;

		public static Direction get(String str) {
			str = str.toUpperCase();
			if (str.equals("N")) {
				return Direction.N;
			} else if (str.equals("E")) {
				return Direction.E;
			} else if (str.equals("W")) {
				return Direction.W;
			} else if (str.equals("S")) {
				return Direction.S;
			} else if (str.equals("NE")) {
				return Direction.NE;
			} else if (str.equals("NW")) {
				return Direction.NW;
			} else if (str.equals("SE")) {
				return Direction.SE;
			} else if (str.equals("SW")) {
				return Direction.SW;
			}
			return null;
		}

		public static Direction get(float deltaX, float deltaY)
				throws IllegalStateException {
			float absDeltaX = Math.abs(deltaX), absDeltaY = Math.abs(deltaY);

			if (absDeltaX > absDeltaY) {
				boolean diagonal = absDeltaY > DIAGONAL_THRESHOLD_FLOAT
						* absDeltaX;
				if (deltaX > 0) {
					if (diagonal) {
						return deltaY < 0 ? Direction.NE : Direction.SE;
					} else {
						return Direction.E;
					}
				} else {
					if (diagonal) {
						return deltaY < 0 ? Direction.NW : Direction.SW;
					} else {
						return Direction.W;
					}
				}
			} else {
				boolean diagonal = absDeltaX > DIAGONAL_THRESHOLD_FLOAT
						* absDeltaY;
				if (deltaY > 0) {
					if (diagonal) {
						return deltaX < 0 ? Direction.SW : Direction.SE;
					} else {
						return Direction.S;
					}
				} else {
					if (diagonal) {
						return deltaX < 0 ? Direction.NW : Direction.NE;
					} else {
						return Direction.N;
					}
				}
			}
		}
	}
}
