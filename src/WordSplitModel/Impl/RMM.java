package WordSplitModel.Impl;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author quincy1994
 */
public class RMM {
    private String m_sResult = "";         //�зֺ�Ľ����
    private int m_nPosIndex;                //�α�ָ��
    private int m_MaxLen;                    //���ȡ�ʳ�
    private int totalMaxlen;                //�����ȡ�ʳ�
    private Set<String> dictionary;      //�ִ��ֵ�
    
    public RMM(int maxLen){
        this.m_MaxLen = maxLen;
        this.totalMaxlen = maxLen;
        try {
            this.dictionary = loadFile();
        } catch (IOException ex) {
            Logger.getLogger(RMM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public RMM(){
        this.m_MaxLen = 5;
        this.totalMaxlen = 5;
        try {
            this.dictionary = loadFile();
        } catch (IOException ex) {
            Logger.getLogger(RMM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @SuppressWarnings("resource")
	public Set<String> loadFile() throws IOException{
        Set<String> dictionary = new HashSet<String>();
        String filename = "dictory.txt";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String tmp;
        while((tmp=br.readLine())!= null){
            String[] token = tmp.split(",");
            String word = token[0];
            dictionary.add(word);
        }
        return dictionary;
    }
    @SuppressWarnings("unused")
	public String RMMSegment(String source){
        int len= totalMaxlen;
        this.m_nPosIndex = source.length();
        int frompos = this.m_nPosIndex;
        rmm(source, m_MaxLen, m_nPosIndex);
   
        //�������˳�����
        String[] token = m_sResult.split("/");
        String result = "";
        for(int i = token.length-1; i > 0 ; i--){
            result += token[i] + "/ ";
        }
        return result;
    }
    
    public String getSubString(String source, int m_nPosIndex, int len){
        
        int startIndex = m_nPosIndex - len;
        while(startIndex < 0){
            startIndex += 1;
        }
        String sub = source.substring(startIndex, m_nPosIndex);
        return sub;
    }
    
    public void rmm(String source, int len, int frompos){
         if(m_nPosIndex < 0)  return;
         String sub = getSubString(source, m_nPosIndex, len);
         if(dictionary.contains(sub)){
             //ƥ��ɹ�
             m_sResult += "/" + sub ;
             m_nPosIndex = m_nPosIndex - m_MaxLen;
             m_MaxLen = totalMaxlen;
             rmm(source, m_MaxLen, m_nPosIndex);
         }
         else{
             //��ƥ��
             if(m_MaxLen > 1){
                 m_MaxLen = m_MaxLen - 1;
                 rmm(source, m_MaxLen, m_nPosIndex);
             }
             else{
//                 m_sResult += "/�ֵ���û�У�" + sub + "����";
                 m_sResult += "/" + sub ;
                 m_nPosIndex -= 1;
                 m_MaxLen = totalMaxlen;
                 rmm(source, m_MaxLen, m_nPosIndex);
            }
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        RMM myRMM = new RMM(10);
        String source = "����һ���У��ĸ￪�ź��ִ������������ǰ���������ñ����ˡ�����������ͨ�͡������÷�չ̬�ơ�ũҵ�����ٴλ�úõ��ճɣ���ҵ�ĸ����������������һ�����ơ����⾭�ü��������뽻����������";
        String result = myRMM.RMMSegment(source);
        System.out.println(result);
    } 
}