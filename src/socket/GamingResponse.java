package socket;

public class GamingResponse {
    private int move;
    private boolean active;

    public GamingResponse(){
        this.move = move;
        this.active = active;
    }

    public GamingResponse(int move, boolean active){
        this.move = move;
        this.active = active;
    }

    public int getMove(){ return move; }

    public boolean getActive(){ return active; }

    public void setMove(int movew){ this.move = move; }

    public void setActive(boolean active){ this.active = active;}


}
