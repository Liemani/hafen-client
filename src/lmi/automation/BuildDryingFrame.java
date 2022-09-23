//  package lmi.automation;
//  
//  import haven.Gob;
//  import haven.Coord;
//  
//  import lmi.*;
//  import static lmi.Api.*;
//  import static lmi.Constant.ExceptionType.*;
//  import static lmi.Constant.BoundingBox.*;
//  import static lmi.Constant.TimeOut.*;
//  
//  public class BuildDryingFrame extends lmi.AutomationManager.Automation {
//      private Rect _input;
//      private Rect _output;
//  
//      private Coord _iOrigin;
//      private Coord _oOrigin;
//  
//      private Coord _matrixSize;
//      private Coord _matrixCoord;
//      private Coord _targetMapCoord;
//      private Coord _moveMapCoord;
//  
//      private Coord _moveCoord;
//  
//      private Array<Gob> _logArrayToCarry;
//  
//      // Run
//      public void run() {
//          LMIException result = null;
//          try {
//              _willRun();
//              _main();
//          } catch (LMIException e) {
//              result = e;
//          }
//          _didRun(result);
//      }
//  
//      // private methods
//      private void _willRun() {
//          alert("정리할 통나무가 있는 곳을 선택해주세요");
//          _input = getArea();
//  
//          alert("통나무를 정리해 놓을 곳을 선택해주세요");
//          _output = getArea();
//  
//          _checkAreaException();
//          _init();
//      }
//  
//      /// - Throws:
//      ///     - ET_NO_SPACE_LEFT
//      private void _checkAreaException() {
//          _tooSmallAreaException();
//      }
//  
//      private void _tooSmallAreaException() {
//          if (_output.width() < BW_OLDTRUNK || _output.height() < BH_OLDTRUNK)
//              throw new LMIException(ET_NO_SPACE_LEFT);
//      }
//  
//      // Initialize
//      private void _init() {
//          _initOrigin();
//          _initMatrixSize();
//          _initMatrix();
//          _initTargetPutCoord();
//          _initTargetMoveCoord();
//          _initMoveCoord();
//      }
//  
//      private void _initOrigin() {
//          _iOrigin = Coord.of(_output.maxX(), _output.maxY())
//              .assignAdd(BB_BODY.divide(2));
//      }
//  
//      private void _initMatrixSize() {
//          final int rowSet = BH_OLDTRUNK + BH_BODY + BH_OLDTRUNK;
//          final int x = _output.width() / BW_OLDTRUNK;
//          final int y = (_output.height() / rowSet) * 2
//              + ((_output.height() % rowSet >= BH_OLDTRUNK) ? 1 : 0);
//          _matrixSize = Coord.of(x, y);
//      }
//  
//      private void _initMatrix() { _matrixCoord = Coord.zero(); }
//  
//      private void _initTargetPutCoord() {
//          _targetMapCoord = new Coord();
//          _calculateTargetPutCoord();
//      }
//  
//      private void _initTargetMoveCoord() {
//          _moveMapCoord = new Coord();
//          _calculateTargetMoveCoord();
//      }
//  
//      private void _initMoveCoord() { _moveCoord = Coord.of(_iOrigin); }
//  
//      // Main
//      private void _main() {
//          if (_coordIsPossessed(_targetMapCoord))
//              _calculateNextCoord();
//  
//          while (true) {
//              try {
//                  _loop();
//              } catch (LMIException e) {
//                  if (e.type() != ET_NO_INPUT) throw e;
//                  alert("추가 통나무를 기다려요");
//                  sleep(TO_WAIT);
//              }
//          }
//      }
//  
//      private void _loop() {
//          forceMove(_iOrigin);
//          forceLift(_findLogToCarry());
//          forceMove(_iOrigin);
//          while (true) {
//              forceMove(_moveCoord.init(_moveCoord.x, _moveMapCoord.y));
//              while (true) {
//                  forceMove(_moveCoord.init(_moveMapCoord.x, _moveCoord.y));
//                  try {
//                      put(_targetMapCoord);
//                      _calculateNextCoord();
//                      break;
//                  } catch (LMIException e) {
//                      if (e.type() != ET_PUT) throw e;
//                      _calculateNextCoord();
//  //                      if (_matrixCoord.y != _previousMatrix.y) {
//  //                          forceMove(_moveCoord.init(_iOrigin.x, _moveCoord.y));
//  //                          break;
//  //                      }
//                  }
//              }
//              if (!Self.gob().isLifting()) break;
//          }
//          forceMove(_moveCoord.init(_iOrigin.x, _moveCoord.y));
//          forceMove(_iOrigin);
//      }
//  
//      /// - Throws
//      ///     - ET_NO_INPUT
//      private Gob _findLogToCarry() {
//          _logArrayToCarry = gobArrayIn(_input)
//              .compactMap(gob -> gob.isLog() ? gob : null);
//  
//          if (_logArrayToCarry.isEmpty())
//              throw new LMIException(ET_NO_INPUT);
//  
//          return closestGobIn(_logArrayToCarry);
//      }
//  
//      /// - Throws
//      ///     - ET_NO_SPACE_LEFT
//      private void _calculateNextCoord() {
//          while (true) {
//              _calculateNextMatrix();
//              _calculateTargetPutCoord();
//              _calculateTargetMoveCoord();
//              if (!_coordIsPossessed(_targetMapCoord)) break;
//          }
//      }
//  
//      /// - Throws
//      ///     - ET_NO_SPACE_LEFT
//      private void _calculateNextMatrix() {
//          ++_matrixCoord.x;
//          if (_matrixCoord.x == _matrixSize.x) {
//              _matrixCoord.x = 0;
//              ++_matrixCoord.y;
//          }
//  
//          if (_matrixCoord.y == _matrixSize.y)
//              throw new LMIException(ET_NO_SPACE_LEFT);
//      }
//  
//      private void _calculateTargetPutCoord() {
//          _targetMapCoord.init(_output.origin)
//              .assignAdd(_matrixCoord.multiply(BB_OLDTRUNK))
//              .assignAdd(BB_OLDTRUNK.divide(2))
//              .assignAdd(0, ((_matrixCoord.y + 1) / 2) * BH_BODY);
//      }
//  
//      private void _calculateTargetMoveCoord() {
//          _moveMapCoord.init(_targetMapCoord);
//          _moveMapCoord.y += ((BH_OLDTRUNK + BH_BODY) / 2)
//              * ((_matrixCoord.y % 2 == 0) ? 1 : -1);
//      }
//  
//      private boolean _coordIsPossessed(Coord coord) {
//          java.util.ArrayList<Gob> gobArray = gobArray();
//          for (Gob gob : gobArray) {
//              if (gob.isAt(coord))
//                  return true;
//          }
//          return false;
//      }
//  
//      private void _didRun(LMIException e) {
//          if (e == null) {
//              alert("모든 통나무를 다 정리했어요");
//              return;
//          }
//  
//          switch (e.type()) {
//              case ET_INTERRUPTED:
//                  alert("작업을 중단했어요");
//                  break;
//              case ET_NO_SPACE_LEFT:
//                  alert("통나무를 둘 남은 공간이 없어요");
//                  break;
//              case ET_NO_INPUT:
//                  alert("모든 통나무를 다 정리했어요");
//                  break;
//              default:
//                  throw e;
//          }
//      }
//  
//      public static String man() {
//          return
//              "설  명: 널브러진 통나무들을 지정한 공간에 차곡차곡 정리합니다\n" +
//              "\n" +
//              "주의 사항: 캐릭터가 이동 시 외부 요인에 의해 멈추면 하려던 작업이 제대로 수행 될 때까지 재시도 합니다\n" +
//              "\n" +
//              "1. 통나무가 있는 공간 선택\n" +
//              "2. 통나무를 쌓아 둘 공간 선택\n" +
//              "3. 통나무를 정리합니다\n" +
//              "4. 모든 통나무를 정리한 후에는 1분 간격으로 추가 통나무가 존재하는지 확인합니다\n" +
//              "";
//      }
//  }
