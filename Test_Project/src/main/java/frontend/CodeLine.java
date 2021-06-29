package frontend;

public class CodeLine {

    private final String content;
    private boolean isBreakpoint;
    private boolean isActive;

    public CodeLine(String content) {
        this.content = content;
        this.isBreakpoint = false;
    }

    public String getContent() {
        return this.content;
    }

    public boolean isBreakpoint() {
        return this.isBreakpoint;
    }

    public void setBreakpoint(boolean breakpoint) {
        this.isBreakpoint = breakpoint;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
