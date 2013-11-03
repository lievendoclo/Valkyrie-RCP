package org.valkyriercp.rules.constraint;

/**
 * @author Keith Donald
 */
public abstract class Operator {

    private String code;
    private String label;

	protected Operator(String code, String symbol) {
		this.code = code;
        this.label = symbol;
	}

	public String getSymbol() {
		return getLabel();
	}

	public Operator negation() {
		return null;
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

    public String toString() {
		return getSymbol();
	}
}
