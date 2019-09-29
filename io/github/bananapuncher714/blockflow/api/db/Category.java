package io.github.bananapuncher714.blockflow.api.db;

public class Category {
	protected String identifier;
	
	public Category() {
	}
	
	public Category( String id ) {
		this.identifier = id;
	}
	
	public void setIdentifier( String id ) {
		this.identifier = id;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
}
