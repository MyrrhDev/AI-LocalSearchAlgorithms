package bicingoptimiser;

public class Pair {
    private Integer first;
    private Integer second;
    
    
    public void setFirst(Integer first) {
        this.first = first;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }
    

    public Integer getFirst() {
        return first;
    }

    public Integer getSecond() {
        return second;
    }

    public Pair(Integer a, Integer b) {
        first = a;
        second = b;
    }

    public String toString() {
        StringBuffer S = new StringBuffer();
        S.append(first.toString()+","+second.toString()+'\n');
        return S.toString();
    }
}
