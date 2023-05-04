package io.wisoft.capstonedesign.domain.hospital.web.dto;


import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * XML Dom 구조를 객체로 표현
 * 해당 모델만 잘 정의해 놓으면 BatchScheduler를 참고하여
 * XML Response를 쉽게 Java Class로 변환할 수 있다.
 *
 * <response>
 *     <header>
 *         <resultCode>00</resultCode>
 *         <resultMsg>NORMAL SERVICE.</resultMsg>
 *     </header>
 *     <body>
 *         <items>
 *             <item>
 *                 <addr>인천광역시 부평구 동수로 56, (부평동)</addr>
 *                 <clCd>01</clCd>
 *                 <clCdNm>상급종합</clCdNm>
 *                 <cmdcGdrCnt>0</cmdcGdrCnt>
 *                 <cmdcIntnCnt>0</cmdcIntnCnt>
 *                 <cmdcResdntCnt>0</cmdcResdntCnt>
 *                 <cmdcSdrCnt>0</cmdcSdrCnt>
 *                 <detyGdrCnt>1</detyGdrCnt>
 *                 <detyIntnCnt>0</detyIntnCnt>
 *                 <detyResdntCnt>0</detyResdntCnt>
 *                 <detySdrCnt>2</detySdrCnt>
 *                 <drTotCnt>333</drTotCnt>
 *                 <emdongNm>부평동</emdongNm>
 *                 <estbDd>19810806</estbDd>
 *                 <hospUrl>http://www.cmcism.or.kr/</hospUrl>
 *                 <mdeptGdrCnt>0</mdeptGdrCnt>
 *                 <mdeptIntnCnt>23</mdeptIntnCnt>
 *                 <mdeptResdntCnt>71</mdeptResdntCnt>
 *                 <mdeptSdrCnt>236</mdeptSdrCnt>
 *                 <pnursCnt>0</pnursCnt>
 *                 <postNo>21431</postNo>
 *                 <sgguCd>220003</sgguCd>
 *                 <sgguCdNm>인천부평구</sgguCdNm>
 *                 <sidoCd>220000</sidoCd>
 *                 <sidoCdNm>인천</sidoCdNm>
 *                 <telno>032-1544-9004</telno>
 *                 <XPos>126.7248987</XPos>
 *                 <YPos>37.4848309</YPos>
 *                 <yadmNm>가톨릭대학교인천성모병원</yadmNm>
 *                 <ykiho>JDQ4MTYyMiM1MSMkMSMkMCMkODkkMzgxMzUxIzExIyQxIyQzIyQ3OSQyNjE4MzIjNDEjJDEjJDgjJDgz</ykiho>
 *             </item>
 *         </items>
 *         <numOfRows>10</numOfRows>
 *         <pageNo>1</pageNo>
 *         <totalCount>76556</totalCount>
 *     </body>
 * </response>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class HospitalInfoResponse {

    private Header header;
    private Body body;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Body {

        @XmlElementWrapper(name = "items")
        @XmlElement(name = "item")
        private List<Item> items = new ArrayList<>();
        private String numOfRows;
        private String pageNo;
        private String totalCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "item")
    public static class Item {
        private String addr;
        private String clCd;
        private String clCdNm;
        private String cmdcGdrCnt;
        private String cmdcIntnCnt;
        private String cmdcResdntCnt;
        private String cmdcSdrCnt;
        private String detyGdrCnt;
        private String detyIntnCnt;
        private String detyResdntCnt;
        private String detySdrCnt;
        private String drTotCnt;
        private String estbDd;
        private String hospUrl;
        private String mdeptGdrCnt;
        private String mdeptIntnCnt;
        private String mdeptResdntCnt;
        private String mdeptSdrCnt;
        private String pnursCnt;
        private String postNo;
        private String sgguCd;
        private String sgguCdNm;
        private String sidoCd;
        private String sidoCdNm;
        private String telno;
        private String XPos;
        private String YPos;
        private String yadmNm;
        private String ykiho;
    }
}
