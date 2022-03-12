package slash;

import java.util.Arrays;
import java.util.List;

public interface ISlash {
	
	String getName();
	
	String getHelp();
	
	default List<String> getAliases(){
		return Arrays.asList();
	}

}
