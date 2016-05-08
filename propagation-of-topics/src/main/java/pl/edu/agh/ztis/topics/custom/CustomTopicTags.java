package pl.edu.agh.ztis.topics.custom;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CustomTopicTags {

	private final List<String> customTags = Lists.newArrayList();
	private final Map<String, List<String>> keywordsMap = Maps.newHashMap();
	
	public CustomTopicTags() {
		customTags.add("Obama");
		keywordsMap.put("Obama", Lists.newArrayList("obama", "obama's", "us president", "president of us" ));
		
		customTags.add("Terrorism");
		keywordsMap.put("Terrorism", Lists.newArrayList("suicide attack", "terrorism", "terrorists"));
		
		customTags.add("Islam");
		keywordsMap.put("Islam", Lists.newArrayList("islam", "islamic", "jihad"));

		customTags.add("jihad");
		keywordsMap.put("jihad", Lists.newArrayList("jihad", "jihadist"));
		
		customTags.add("al-Qa'ida");
		keywordsMap.put("al-Qa'ida", Lists.newArrayList("al-qa'ida"));
		
	}

	public List<String> getCustomTopicTags() {
		return customTags;
	}

	public List<String> getKeywordsForTopic(String topicTag) {
		return keywordsMap.get(topicTag);
	}
	

	
}
