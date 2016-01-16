package util;

public enum EnumCorner {
	
	TOPLEFT {
		@Override
		public boolean top() {
			return true;
		}
		@Override
		public boolean left() {
			return true;
		}
	},
	TOP {
		@Override
		public boolean top() {
			return true;
		}
	},
	TOPRIGHT {
		@Override
		public boolean top() {
			return true;
		}
		@Override
		public boolean right() {
			return true;
		}
	},
	LEFT {
		@Override
		public boolean left() {
			return true;
		}
	},
	CENTER,
	RIGHT {
		@Override
		public boolean right() {
			return true;
		}
	},
	BOTTOMLEFT {
		@Override
		public boolean bottom() {
			return true;
		}
		@Override
		public boolean left() {
			return true;
		}
	},
	BOTTOM {
		@Override
		public boolean bottom() {
			return true;
		}
	},
	BOTTOMRIGHT {
		@Override
		public boolean bottom() {
			return true;
		}
		@Override
		public boolean right() {
			return true;
		}
	};
	
	
	public boolean top() {
		return false;
	}
	public boolean bottom() {
		return false;
	}
	public boolean left() {
		return false;
	}
	public boolean right() {
		return false;
	}
	
}
