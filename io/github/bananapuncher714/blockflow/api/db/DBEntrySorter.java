package io.github.bananapuncher714.blockflow.api.db;

import java.util.Collection;

public interface DBEntrySorter {
	DBEntry[] sort( Collection< DBEntry > entries );
}
