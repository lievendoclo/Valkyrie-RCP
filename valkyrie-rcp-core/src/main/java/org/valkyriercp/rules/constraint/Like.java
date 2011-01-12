package org.valkyriercp.rules.constraint;

import org.springframework.core.enums.StringCodedLabeledEnum;
import org.springframework.util.StringUtils;

/**
 * A like constraint, supporting "starts with%", "%ends with", and "%contains%".
 *
 * @author Keith Donald
 */
public class Like implements Constraint {

	public static final LikeType STARTS_WITH = new LikeType("startsWith");

	public static final LikeType ENDS_WITH = new LikeType("endsWith");

	public static final LikeType CONTAINS = new LikeType("contains");

	private LikeType type;

	private String stringToMatch;

	public Like(LikeType type, String likeString) {
		this.type = type;
		this.stringToMatch = likeString;
	}

	public Like(String encodedLikeString) {
		if (encodedLikeString.startsWith("%")) {
			if (encodedLikeString.endsWith("%")) {
				this.type = CONTAINS;
			}
			else {
				this.type = ENDS_WITH;
			}
		}
		else if (encodedLikeString.endsWith("%")) {
			this.type = STARTS_WITH;
		}
		else {
			this.type = CONTAINS;
		}
		stringToMatch = StringUtils.deleteAny(encodedLikeString, "%");
	}

	public boolean test(Object argument) {
		String value = String.valueOf(argument);
		if (type == STARTS_WITH) {
			return value.startsWith(stringToMatch);
		}
		else if (type == ENDS_WITH) {
			return value.endsWith(stringToMatch);
		}
		else {
			return value.indexOf(stringToMatch) != -1;
		}
	}

	public LikeType getType() {
		return type;
	}

	public String getString() {
		return stringToMatch;
	}

    public static class LikeType {
        private String code;
        private String label;

        public LikeType(String code, String label) {
            this.code = code;
            this.label = label;
        }

        private LikeType(String code) {
            this(code, null);
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

}
