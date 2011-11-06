package org.osgcc.osgcc5.soapydroid.things;

import android.graphics.Bitmap;

public abstract class CollidableThing {

	// position
	protected float x;
	protected float y;

	// velocity
	protected float dx;
	protected float dy;

	// acceleration
	protected float accx;
	protected float accy;

	// mass
	protected float mass;

	// size
	protected float height;
	protected float width;

	// bitmap
	protected Bitmap bitmap;

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public float getAccx() {
		return accx;
	}

	public void setAccx(float accx) {
		this.accx = accx;
	}

	public float getAccy() {
		return accy;
	}

	public void setAccy(float accy) {
		this.accy = accy;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}
}
