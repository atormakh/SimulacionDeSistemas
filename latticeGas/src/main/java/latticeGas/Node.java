package latticeGas;

public class Node {

    char in_state;
    char out_state;

    final char R = 1 << 6;
    final char S = 1 << 7;
    final char VW = 1 << 8; // vertical wall
    final char HW = 1 << 9; // horizontal wall

    final int DIRECTIONS = 6;

    final char PROPS = R | S | VW | HW;

    char[] rotations = {
            0b001001, 0b010010, 0b100100,
            0b010101, 0b101010,
            0b110110, 0b101101, 0b011011,
    };

    char [] verticalBounce={0b001000,0b000100,0b000010,0b000001,0b100000,0b010000};
    char[] horizontalBounce = {0,0b100000,0b010000,0,0b000100,0b000010};

    public Node(double random) {
        in_state = out_state =  random < 0.5 ? R : 0;

    }

    public void setVerticalWall(boolean b) {
        if (b) {
            in_state |= VW;
            out_state |= VW;
        } else {
            in_state &= ~VW;
            out_state &= ~VW;
        }
    }

    public void setHorizontalWall(boolean b) {
        if (b) {
            in_state |= HW;
            out_state |= HW;
        } else {
            in_state &= ~HW;
            out_state &= ~HW;
        }
    }

    public void setBoundary(boolean b) {
        if (b) {
            in_state |= S;
            out_state |= S;
        } else {
            in_state &= ~S;
            out_state &= ~S;
        }

    }

    public void setInDirection(int direction, boolean b) {
        if (b) {
            in_state |= (1 << direction);
        } else {
            in_state &= ~(1 << direction);
        }
    }


    public boolean getOutDirection(int direction) {
        return (out_state & (1 << direction)) != 0;
    }

    public boolean getInDirection(int direction) {
        return (in_state & (1 << direction)) != 0;
    }



    public char rotateDirection(char bits, boolean right) {
        return right ? (char) (bits << 1 | bits >> 5) : (char) (bits >> 1 | bits << 5);
    }

    public void recalculate() {
        out_state = in_state;
        in_state = (char) (out_state & PROPS);

        char directions = (char) (out_state & ~PROPS);

        char new_state = 0;

        if ((out_state & S) != 0) {
            if((out_state & VW) != 0 && (out_state & HW) != 0) // corner
                new_state = (char) (directions << 3 | directions >> 3);
            else{
                char[] bounce = (out_state & VW) != 0 ? verticalBounce : horizontalBounce;
                for(int i = 0; i< DIRECTIONS;i++) {
                    if ((directions & (1 << i)) != 0) {
                        new_state |= bounce[i];
                    }
                }
            }
            out_state = new_state;
            out_state |= (char) (in_state & PROPS);
            return;
        }

        for (char rotation : rotations) {
            if (rotation == directions) {
                out_state = rotateDirection(directions, (out_state & R) != 0);
                out_state |= (char) (in_state & PROPS);
                return;
            }


        }
    }
}
