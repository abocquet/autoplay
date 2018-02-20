package yamlbeans.document;

import java.io.IOException;

import yamlbeans.YamlConfig.WriteConfig;
import yamlbeans.emitter.Emitter;
import yamlbeans.emitter.EmitterException;
import yamlbeans.parser.AliasEvent;

public class YamlAlias extends YamlElement {

	@Override
	public void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException {
		emitter.emit(new AliasEvent(anchor));
	}
	
	@Override
	public String toString() {
		return "*" + anchor;
	}
}
