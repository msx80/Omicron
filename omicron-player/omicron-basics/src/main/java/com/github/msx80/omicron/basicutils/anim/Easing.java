package com.github.msx80.omicron.basicutils.anim;

public enum Easing {

	
	// from https://github.com/tweenjs/tween.js/blob/master/src/Tween.js
	LINEAR(t -> t),
	QUADRATIC_IN(t -> t*t),
	QUADRATIC_OUT(t -> t * (2d - t)),
	BOUNCE_OUT(k -> {
		if (k < (1d / 2.75d)) {
			return 7.5625d * k * k;
		} else if (k < (2d / 2.75d)) {
			return 7.5625d * (k -= (1.5d / 2.75d)) * k + 0.75d;
		} else if (k < (2.5d / 2.75d)) {
			return 7.5625d * (k -= (2.25d / 2.75d)) * k + 0.9375d;
		} else {
			return 7.5625d * (k -= (2.625d / 2.75d)) * k + 0.984375d;
		}
	}),
	BOUNCE_IN(k-> 1d - Easing.BOUNCE_OUT.fun.apply(1d - k)),
	BOUNCE_INOUT(k-> {
		if (k < 0.5d) {
			return BOUNCE_IN.fun.apply(k * 2d) * 0.5d;
		}

		return BOUNCE_OUT.fun.apply(k * 2d - 1d) * 0.5d + 0.5d;
	}),
	
	ELASTIC_IN(k -> {
		if (k == 0) {
			return 0;
		}

		if (k == 1) {
			return 1;
		}

		return (-Math.pow(2d, 10 * (k - 1d)) * Math.sin((k - 1.1d) * 5d * Math.PI));
	}),
	BACK_IN( k-> {
		double s = 1.70158;
			
			return k * k * ((s + 1) * k - s);
			
	})
	
	;
	
	
	
	
	/*
	 
	Quadratic: {

		In: function (k) {

			return k * k;

		},

		Out: function (k) {

			return k * (2 - k);

		},

		InOut: function (k) {

			if ((k *= 2) < 1) {
				return 0.5 * k * k;
			}

			return - 0.5 * (--k * (k - 2) - 1);

		}

	},
	 */
	public final DoubleFunction fun;

	Easing(DoubleFunction fun)
	{
		this.fun = fun;
	};
	
	
}
