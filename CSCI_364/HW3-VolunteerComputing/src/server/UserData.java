//Sam Dressler
//UserData.java
package server;

public class UserData
{
    
    private int score = 0;
    private String id;
    public UserData(String userID)
    {
        this.id = userID;
        this.score = 0;
    }
    public void incrementScore()
    {
        this.score++;
    }
    public int getScore()
    {
        return this.score;
    
    }
    public void resetScore()
    {
        this.score = 0;
    }
}