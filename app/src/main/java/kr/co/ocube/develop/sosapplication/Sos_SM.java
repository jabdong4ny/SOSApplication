package kr.co.ocube.develop.sosapplication;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



class Sos_SM extends StateMachine {
    public static final int CMD_TRANSITION_TO_IDLE      = 1;
    public static final int CMD_TRANSITION_TO_ACTIVE    = 2;
    public static final int CMD_TRANSITION_TO_CALLBACK  = 3;

    public static final int EVT_BUTTON_EVENT              = 0XF1;
    private static final String TAG = "Sos_SM";
    private static Handler mHandler;

    IdleState m_IdleState            = new IdleState();
    CallbackState m_CallbackState   = new CallbackState();
    ActiveState m_ActiveState       = new ActiveState();
    Sos_SM(String name) {
        super(name);
        Log.d(TAG, "ctor E");

        setHandler();

        // Add states, use indentation to show hierarchy
        addState(m_IdleState);
        addState(m_CallbackState);
//        addState(mS2, mP1); can setting parent state
        addState(m_ActiveState);
        // Set the initial state
        setInitialState(m_IdleState);
        Log.d(TAG, "ctor X");
        start(); // sate machine start!!
    }

    private void setHandler(){
        m_IdleState.setHandler(mHandler);
        m_CallbackState.setHandler(mHandler);
        m_ActiveState.setHandler(mHandler);
    }

    private static class SmHolder{
        public static final Sos_SM instance = new Sos_SM("sm1");
    }

    public static Sos_SM getInstance(){
        return SmHolder.instance;
    }

    public static Sos_SM getInstance(Handler hd){
        mHandler = hd;
        return SmHolder.instance;
    }

    @Override
    protected void onHalting() {
        Log.d(TAG, "halting");
        synchronized (this) {
            this.notifyAll();
        }
    }

    public boolean tryToTransmit(int where) {
        boolean retVal;
        Log.d(TAG, "tryToTransmit where=" + where);
        switch (where) {
            case (CMD_TRANSITION_TO_IDLE):
                transitionTo(m_IdleState);
                retVal = HANDLED;
                break;
            case (CMD_TRANSITION_TO_ACTIVE):
//                deferMessage(message);
                transitionTo(m_ActiveState);
                retVal = HANDLED;
                break;
            case (CMD_TRANSITION_TO_CALLBACK):
//                deferMessage(message);
                transitionTo(m_CallbackState);
                retVal = HANDLED;
                break;
            default:
                retVal = NOT_HANDLED;
                break;
        }
        return retVal;
    }

/*    class P1 extends State {
        @Override
        public void enter() {
            Log.d(TAG, "mP1.enter");
        }

        @Override
        public boolean processMessage(Message message) {
            boolean retVal;
            Log.d(TAG, "mP1.processMessage what=" + message.what);
            switch (message.what) {
                case CMD_2:
                    // CMD_2 will arrive in mS2 before CMD_3
                    sendMessage(obtainMessage(CMD_3));
                    deferMessage(message);
                    transitionTo(m_ActiveState);
                    retVal = HANDLED;
                    break;
                default:
                    // Any message we don't understand in this state invokes unhandledMessage
                    retVal = NOT_HANDLED;
                    break;
            }
            return retVal;
        }

        @Override
        public void exit() {
            Log.d(TAG, "mP1.exit");
        }
    }

    class S1 extends State {
        @Override
        public void enter() {
            Log.d(TAG, "mS1.enter");
        }

        @Override
        public boolean processMessage(Message message) {
            Log.d(TAG, "S1.processMessage what=" + message.what);
            if (message.what == CMD_1) {
                // Transition to ourself to show that enter/exit is called
                transitionTo(m_IdleState);
                return HANDLED;
            } else {
                // Let parent process all other messages
                return NOT_HANDLED;
            }
        }

        @Override
        public void exit() {
            Log.d(TAG, "mS1.exit");
        }
    }

    class S2 extends State {
        @Override
        public void enter() {
            Log.d(TAG, "mS2.enter");
        }

        @Override
        public boolean processMessage(Message message) {
            boolean retVal;
            Log.d(TAG, "mS2.processMessage what=" + message.what);
            switch (message.what) {
                case (CMD_2):
                    sendMessage(obtainMessage(CMD_4));
                    retVal = HANDLED;
                    break;
                case (CMD_3):
                    deferMessage(message);
                    transitionTo(m_CallbackState);
                    retVal = HANDLED;
                    break;
                default:
                    retVal = NOT_HANDLED;
                    break;
            }
            return retVal;
        }

        @Override
        public void exit() {
            Log.d(TAG, "mS2.exit");
        }
    }

    class P2 extends State {
        @Override
        public void enter() {
            Log.d(TAG, "mP2.enter");
            sendMessage(obtainMessage(CMD_5));
        }

        @Override
        public boolean processMessage(Message message) {
            Log.d(TAG, "P2.processMessage what=" + message.what);
            switch (message.what) {
                case (CMD_3):
                    break;
                case (CMD_4):
                    break;
                case (CMD_5):
                    transitionToHaltingState();
                    break;
            }
            return HANDLED;
        }

        @Override
        public void exit() {
            Log.d(TAG, "mP2.exit");
        }
    }*/
}