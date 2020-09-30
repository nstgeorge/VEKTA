package vekta.terrain.visual;

import static processing.core.PApplet.*;
import static processing.core.PConstants.TWO_PI;
import static vekta.Vekta.v;

public class SettlementVisual extends GroupVisual {
	private final int ringParts = (int)v.random(5, 10);
	private final float ringSpacing = v.random(50, 100);
	private final float ringRandomness = v.random(20, 50);
	private final float roadConnectivity = 2;

	private final float[] xPositions;
	private final float[] yPositions;

	public SettlementVisual(float x, float y, float scale) {
		super(x, y);

		this.xPositions = new float[1 + (int)(30 * sqrt(scale))];
		this.yPositions = new float[xPositions.length];

		// Add center position
		xPositions[0] = 0;
		yPositions[0] = 0;

		int ringIndex = 1;
		int index = 1;
		while(index < xPositions.length) {
			float angleOffset = v.random(TWO_PI);
			for(int i = 0; i < ringParts * sqrt(ringIndex); i++) {
				float angle = TWO_PI * i / ringParts + angleOffset;
				xPositions[index] = cos(angle) * ringIndex * ringSpacing + v.random(ringRandomness) - ringRandomness / 2;
				yPositions[index] = sin(angle) * ringIndex * ringSpacing + v.random(ringRandomness) - ringRandomness / 2;
				if(++index >= xPositions.length) {
					break;
				}
			}
			ringIndex++;
		}

		populate(scale);
	}

	private void populate(float scale) {
		//		int roadCt = (int)(v.random(3, 5) * scale);
		//		for(int i = 0; i < roadCt; i++) {
		//			int a = randomPosition();
		//			int b = randomPosition();
		//			float width = v.random(1, 3);
		//			if(a != b) {
		//				add(new RoadVisual(xPositions[a], yPositions[a], xPositions[b], yPositions[b], width));
		//			}
		//		}

		float roadDistSq = sq(ringSpacing * roadConnectivity);
		for(int a = 0; a < xPositions.length; a++) {
			for(int b = a + 1; b < xPositions.length; b++) {
				if(distSq(a, b) < roadDistSq) {
					float width = v.random(1, 5);
					add(new RoadVisual(xPositions[a], yPositions[a], xPositions[b], yPositions[b], width));
				}
			}
		}

		int domeCt = (int)(v.random(1, 2) * scale);
		for(int i = 0; i < domeCt; i++) {
			int pos = randomPosition();
			int nearest = nearestPosition(pos);
			add(new DomeVisual(xPositions[pos], yPositions[pos], sqrt(distSq(pos, nearest))));
		}

		int buildingCt = (int)(v.random(2, 4) * scale);
		for(int i = 0; i < buildingCt; i++) {
			int pos = randomPosition();
			int nearest = nearestPosition(pos);
			add(new BuildingVisual(xPositions[pos], yPositions[pos], xPositions[nearest], yPositions[nearest],
					v.random(.1F, 1) * ringSpacing,
					v.random(.2F, 1)));
		}
	}

	private int randomPosition() {
		return (int)v.random(xPositions.length);
	}

	private float distSq(int a, int b) {
		return sq(xPositions[a] - xPositions[b]) + sq(yPositions[a] - yPositions[b]);
	}

	private int nearestPosition(int pos) {
		float minSq = Float.POSITIVE_INFINITY;
		int nearest = pos;
		for(int i = 0; i < xPositions.length; i++) {
			float distSq = distSq(pos, i);
			if(i != pos && distSq < minSq) {
				minSq = distSq;
				nearest = i;
			}
		}
		return nearest;
	}

	@Override
	public void draw() {
		super.draw();
		///
		//		for(int i = 0; i < xPositions.length; i++) {
		//			v.ellipse(xPositions[i], yPositions[i], 5, 5);
		//		}
	}
}
