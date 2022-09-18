// why not using instance?
// message는 String으로 식별하기 때문에 자원 소모가 많아 1개의 wait만 지원한다
// Signal은 enum이기 때문에 자원 소모가 적어 여러 wait을 지원한다
//
// least work for main thread
// notify에서 많은 추가 작업을 하면 main thread가 일을 해야 해서 좋지 않다
// 받은 Signal에 대해 notify하고 해당 waiter를 list에서 remove하는 작업까지만 한다
package lmi;

import java.util.LinkedList;
import java.util.EnumMap;

import lmi.Constant.Signal;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.Signal.*;
import static lmi.Constant.TimeOut.*;

public class WaitManager {
    // Define
    static class WaiterList extends LinkedList<SignalWaiter> {}

    // Field
    private static EnumMap<Signal, WaiterList> _waiterListMap;
    private static String _message;

    // Init
    static void init() {
        _waiterListMap = new EnumMap<Signal, WaiterList>(Signal.class);
        for (Signal signal : Signal.values())
            _waiterListMap.put(signal, new WaiterList());
        _message = null;
    }

    // Is Waiting Signal
    public static boolean isWaitingSignal(Signal signal, Object subject) {
        final WaiterList waiterList = _waiterListMap.get(signal);
        synchronized (waiterList) {
            if (waiterList.isEmpty()) return false;
            for (SignalWaiter waiter : waiterList)
                if (waiter._isWaiting(subject))
                    return true;
        }
        return false;
    }

    public static boolean isWaitingSignal(Signal signal) {
        final WaiterList waiterList = _waiterListMap.get(signal);
        synchronized (waiterList) {
            return !waiterList.isEmpty();
        }
    }

    // Wait Signal
    public static void waitSignal(Signal signal, Object subject, long timeOut) {
        final SignalWaiter waiter = new SignalWaiter(signal, subject);
        final WaiterList waiterList = _waiterListMap.get(signal);
        synchronized (waiterList) {
            waiterList.add(waiter);
        }
        waiter._wait(timeOut);
    }

    public static void waitSignal(Signal signal, long timeOut) {
        WaitManager.waitSignal(signal, null, timeOut);
    }

    public static void waitSignal(Signal signal, Object subject) {
        WaitManager.waitSignal(signal, subject, TO_NONE);
    }

    public static void waitSignal(Signal signal) {
        WaitManager.waitSignal(signal, null, TO_NONE);
    }

    // Notify Signal
    /// - Example
    /// if (WaitManager.isWaitingSignal(signal, subject))
    ///     WaitManager.notifySignal(signal, subject);
    public static void notifySignal(Signal signal, Object subject) {
        final WaiterList waiterList = _waiterListMap.get(signal);
        synchronized (waiterList) {
            if (waiterList.isEmpty()) return;

            waiterList.removeIf((signalWaiter) -> {
                    if (signalWaiter._isWaiting(subject)) {
                        signalWaiter._notify();
                        return true;
                    } else
                        return false; });
        }
    }

    public static void notifySignal(Signal signal) {
        notifySignal(signal, null);
    }

    // Message
    public static void waitMessage(String message) {
        _message = message;
        _wait();
    }

    public static void notifyMessage(String message) {
        if (!_isWaiting(message)) return;

        _message = null;
        _notify();
        Util.debugPrint("message: " + message);
    }

    private static boolean _isWaiting(String message) {
        if (_message == null) return false;
        return message.contentEquals(_message);
    }

    private static void _wait() {
        try {
            synchronized (WaitManager.class) {
                WaitManager.class.wait();
            }
        } catch (InterruptedException e) {
            throw new LMIException(ET_INTERRUPTED);
        }
    }

    private static void _notify() {
        synchronized (WaitManager.class) {
            WaitManager.class.notify();
        }
    }

    // SignalWaiter
    private static class SignalWaiter {
        private Signal _signal;
        private Object _subject;

        private SignalWaiter(Signal signal, Object subject) {
            _signal = signal;
            _subject = subject;
        }

        private boolean _isWaiting(Object subject) { return _subject == subject; }

        /// - Throws:
        ///     - ET_TIME_OUT
        private void _wait(long timeOut) {
            final long startTime = System.currentTimeMillis();

            try {
                synchronized (this) {
                    this.wait(timeOut);
                }
            } catch (InterruptedException e) {
                throw new LMIException(ET_INTERRUPTED);
            }

            final long endTime = System.currentTimeMillis();

            if (timeOut != TO_NONE && endTime - startTime >= timeOut)
                throw new LMIException(ET_TIME_OUT);
        }

        private void _notify() {
            synchronized (this) {
                this.notify();
            }
        }
    }
}
