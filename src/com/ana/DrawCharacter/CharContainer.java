package com.ana.DrawCharacter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import android.graphics.PointF;
import android.util.Log;

import com.ana.DrawCharacter.StrokePath.Direction;


public class CharContainer {
	private MultiMap<String, String[]> map;
	private StrokeOrderContainer strokeOrderContainer;

	public CharContainer() {
		if (map == null)
			map = new MultiValueMap<String, String[]>();
	}

	public void appendCharacterData(InputStream is) throws IOException {

		String str = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		while ((str = reader.readLine()) != null) {
			if (str.length() <= 1)
				continue;
			String[] strs = str.split(" ");
			String[] seq = new String[strs.length - 1];
			for (int i = 1; i < strs.length; i++) {
				seq[i - 1] = strs[i];
			}
			map.put(strs[0], seq);
		}
		is.close();
	}

	public void appendStrokeOrderContainer(InputStream is) throws IOException {
		if (strokeOrderContainer == null) {
			strokeOrderContainer = new StrokeOrderContainer();
		}
		strokeOrderContainer.appendStrokeData(is);
	}

	public boolean compareFull(String charname,
			List<StrokePath> characterStrokes) throws NotFoundCharacterExeption {
		@SuppressWarnings("unchecked")
		Collection<String[]> coll = (Collection<String[]>) map.get(charname);
		if (coll == null)
			throw new NotFoundCharacterExeption("Not found character "
					+ charname + " in databases");

		// get a set of strokes in collection
		for (String[] setOfStrokes : coll) {
			if (setOfStrokes.length != characterStrokes.size()) {
				Log.w("MYAPP",
						"Suffer through a set which isn't equal size stroke("
								+ characterStrokes.size() + ") # temp("
								+ setOfStrokes.length + ")");
				continue;
			}
			boolean flag = false;

			// get strokes in set
			for (int j = 0; j < setOfStrokes.length; j++) {
				try {
					if (!strokeOrderContainer.isStrokeEquivalent(
							setOfStrokes[j], characterStrokes.get(j)
									.getReduceStroke(0.05f))) {
						flag = true;
						break;
					}
				} catch (NotFoundStroke e) {
					e.printStackTrace();
				}
			}
			if (flag)
				continue;
			return true;
		}
		return false;
	}

	public int compareStrokeByStroke(String charname,
			List<StrokePath> characterStrokes)
			throws NotFoundCharacterExeption, CompleteCharacterExeption {
		Log.d("MYAPP", "charname "+charname);
		@SuppressWarnings("unchecked")
		Collection<String[]> coll;
		if ((coll = (Collection<String[]>) map.get(charname)) == null)
			throw new NotFoundCharacterExeption("Not found character "+ charname + " in databases");
		// get a set of strokes in collection
		int offset = -1;
		for (String[] setOfStrokes : coll) {

			// get strokes in set
			for (int j = 0; j < characterStrokes.size()
					&& j < setOfStrokes.length; j++) {
				try {
					if (!strokeOrderContainer.isStrokeEquivalent(
							setOfStrokes[j], characterStrokes.get(j)
									.getReduceStroke(0.05f))) {
						break;
					}
					offset = j;
				} catch (NotFoundStroke e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				if (offset == (setOfStrokes.length - 1))
					throw new CompleteCharacterExeption();
			}
		}
		return offset;
	}

	public void registry(String charactername,
			List<StrokePath> characterStrokes, String filepath) {
		String stringToWrite = "";

		for (int i = 0; i < characterStrokes.size(); i++) {
			String strokename = charactername + (i + 1);
			stringToWrite += strokename;

			// get direction vectors
			StrokePath reduce = characterStrokes.get(i).getReduceStroke(0.05f);
			List<Direction> orderdir = reduce.getOrderDirection();
			for (Direction direction : orderdir) {
				stringToWrite += " " + direction.toString();
			}

			// get vector of begin ends and end ends
			PointF beginEnds = reduce.get(0);
			PointF endEnds = reduce.get(reduce.size() - 1);
			float deltaX = endEnds.x - beginEnds.x;
			float deltaY = endEnds.y - beginEnds.y;
			Direction vector = Direction.get(deltaX, deltaY);
			stringToWrite += " " + vector.toString() + "\n";

			// Create Direction[] to update data;
			Direction[] dirs = new Direction[orderdir.size() + 1];
			for (int j = 0; j < orderdir.size(); j++) {
				dirs[j] = orderdir.get(j);
			}
			dirs[dirs.length - 1] = vector;

			strokeOrderContainer.appendStrokeData(strokename, dirs);
		}
		FileWriter filewriter;
		try {
			Log.d("MYAPP", stringToWrite);
			Log.d("MYAPP", filepath);
			filewriter = new FileWriter(filepath, true);
			filewriter.write(stringToWrite);
			filewriter.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public boolean compareStroke(String strokename, StrokePath stroke)
			throws NotFoundStroke {
		return strokeOrderContainer != null ? strokeOrderContainer
				.isStrokeEquivalent(strokename, stroke) : false;
	}

	public void clear() {
		map.clear();
		strokeOrderContainer.clear();

	}

	public void printoutdata() {
		Log.d("MYAPP", "so chu: " +map.size());
		strokeOrderContainer.printoutdata();
//		strokeOrderContainer.printoutdata();
//		Log.d("MYAPP", "----------------");
//		Set<String> keyset = map.keySet();
//		for (String string : keyset) {
//			Log.d("MYAPP", string);
//			Collection<String[]> coll = (Collection<String[]>) map.get(string);
//			for (String[] strings : coll) {
//				String buff = "";
//				for (String string2 : strings) {
//					buff += " " + string2;
//				}
//				Log.d("MYAPP", buff);
//			}
//		}
	}

	public class StrokeOrderContainer {
		private MultiMap<String, Direction[]> map;

		public StrokeOrderContainer() {

		}

		public void clear() {
			map.clear();
		}

		public boolean isStrokeEquivalent(String strokename, StrokePath stroke)
				throws NotFoundStroke {
			Log.d("MYAPP", "Compare stroke " + strokename);
			if (stroke == null)
				throw new NullPointerException(
						"NullPointerException strokepath");
			@SuppressWarnings("unchecked")
			Collection<Direction[]> coll = (Collection<Direction[]>) map
					.get(strokename);
			if (coll == null)
				throw new NotFoundStroke("Not found stroke: " + strokename);

			List<Direction> dirVectors = stroke.getOrderDirection();
			// get a set in collection;
			for (Direction[] disTempls : coll) {

				// if they are different then continue with other set
				if (dirVectors.size() != (disTempls.length - 1))
					continue;
				// check directions in set
				boolean flag = true;
				for (int i = 0; i < dirVectors.size(); i++) {
					if (!dirVectors.get(i).equals(disTempls[i])) {
						flag = false;
						break;
					}
				}
				if (flag) {
					PointF startends = stroke.get(0);
					PointF endends = stroke.get(stroke.size() - 1);
					float deltax = endends.x - startends.x;
					float deltay = endends.y - startends.y;
					if (Direction.get(deltax, deltay).equals(
							disTempls[disTempls.length - 1])) {
						return true;
					}
				}
			}
			return false;
		}

		public void appendStrokeData(String strokename, Direction[] data) {
			map.put(strokename, data);
		}

		public void appendStrokeData(InputStream is) throws IOException {

			if (map == null) {
				map = new MultiValueMap<String, Direction[]>();
			}
			String str = null;
			String[] strs;
			boolean wrong_format;
			int line = 0;
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			while ((str = reader.readLine()) != null) {
				if (str.length() <= 1)
					continue;
				wrong_format = false;
				line++;
				strs = str.split(" ");
				Direction dir;
				Direction[] seq = new Direction[strs.length - 1];
				for (int i = 1; i < strs.length; i++) {
					dir = Direction.get(strs[i]);
					if (dir == null) {
						wrong_format = true;
						str = strs[i];
						break;
					}
					seq[i - 1] = dir;
					// Log.d("MYAPP", dir.toString());

				}
				if (wrong_format) {
					Log.e("MYAPP", "wrong fomat: " + str + " at line " + line);
					continue;
				}
				map.put(strs[0], seq);

			}
			is.close();
		}

		public void printoutdata() {
			Log.d("MYAPP", "So net "+strokeOrderContainer.map.size());
//			Set<String> keyset = map.keySet();
//			for (String string : keyset) {
//				Log.d("MYAPP", string);
//				Collection<Direction[]> coll = (Collection<Direction[]>) map
//						.get(string);
//				for (Direction[] directions : coll) {
//					String buff = "";
//					for (Direction direction : directions) {
//						buff += " " + direction.toString();
//					}
//					Log.d("MYAPP", buff);
//				}
//
//			}
		}

	}

	public class CompleteCharacterExeption extends Exception {
		public CompleteCharacterExeption() {
		}
	}

	public class NotFoundCharacterExeption extends Exception {

		public NotFoundCharacterExeption(String name) {
			super(name);
		}
	}

	class NotFoundStroke extends Exception {
		NotFoundStroke(String mess) {
			super(mess);
		}
	}
}
