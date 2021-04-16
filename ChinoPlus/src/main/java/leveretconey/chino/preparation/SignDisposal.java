package leveretconey.chino.preparation;

import leveretconey.chino.util.Util;

class SignDisposal {
    public static final String INPUT="datasets/echocardiogram.csv";
    public static final String OUTPUT="datasets/echocardiogram done.csv";

    public static void main(String[] args) {
        String input= Util.fromFile(INPUT);
        StringBuilder result=new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c=input.charAt(i);
            if(c!='\"'){
                result.append(c);
            }
        }
        Util.toFile(result.toString(),OUTPUT);
    }
}
