package lmi.macro;

public class Util {
    static haven.Gob gob_;

    public static void storeLastClickedGob(haven.Gob gob) { gob_ = gob; }
    public static void printLastClickedGob() {
        lmi.Debug.debugDescribeField(gob_);
        lmi.Debug.debugDescribeField(gob_.rc);
        lmi.Debug.debugDescribeField(convertCoord2dToCoord(gob_.rc));
    }

    public static void selectCharacter(String name) {
        lmi.ObjectShadow.characterList_.wdgmsg(lmi.Constant.Command.SELECT_CHARACTER, name);
    }

    public static haven.Coord convertCoord2dToCoord(haven.Coord2d point) {
        return new haven.Coord(point.floor(haven.OCache.posres));
    }
}

//  좌표 체계는 double 값으로 11의 배수가 각 타일의 한 꼭지점이다
//  int 로는 어떻게 될까? 변환해보도록 하자
//  int로는 한 타일은 1024의 간격을 갖는다
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10829.5},
//  {"type" : "double","name" : "y","value" : (double)-10708.5},}}
//  "fields" : {{"type" : "int","name" : "x","value" : (int)-1008128},
//  {"type" : "int","name" : "y","value" : (int)-996864},}}
//  
//  +11, 0
//  +1024, 0
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10818.5},
//  {"type" : "double","name" : "y","value" : (double)-10708.5},}}
//  "fields" : {{"type" : "int","name" : "x","value" : (int)-1007104},
//  {"type" : "int","name" : "y","value" : (int)-996864},}}
//  
//  -11, 0
//  -1024, 0
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10829.5},
//  {"type" : "double","name" : "y","value" : (double)-10708.5},}}
//  "fields" : {{"type" : "int","name" : "x","value" : (int)-1008128},
//  {"type" : "int","name" : "y","value" : (int)-996864},}}
//  
//  0, +11
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10829.5},
//  {"type" : "double","name" : "y","value" : (double)-10697.5},}}
//  "fields" : {{"type" : "int","name" : "x","value" : (int)-1008128},
//  {"type" : "int","name" : "y","value" : (int)-995840},}}
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10826.8037109375},
//  {"type" : "double","name" : "y","value" : (double)-10752.2958984375},}}
//  
//  0.0107421875
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10826.79296875},
//  {"type" : "double","name" : "y","value" : (double)-10752.2958984375},}}
//  
//  0.0107421875
//  
//  "fields" : {{"type" : "double","name" : "x","value" : (double)-10826.7822265625},
//  {"type" : "double","name" : "y","value" : (double)-10752.2958984375},}}
