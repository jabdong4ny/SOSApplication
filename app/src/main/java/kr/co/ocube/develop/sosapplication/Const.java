package kr.co.ocube.develop.sosapplication;

// TODO: eML 관리 manager로 대체 필요
public class Const {
    public static final int MSG_SOS_STATE = 1;
    public static final int MSG_CALL_STATUS = 2;
    public static final int MSG_DATA_KIND_OF = 3;
    public static final int MSG_HEAD_UNIT_DISPLAY = 4;
    public static final int MSG_TOAST_UPDATE = 5;

    public static final int MSG_CHANGE_SOS_STATE_UPDATE = 11;
    public static final int MSG_DATA_SEND = 12;
    public static final int MSG_CHANGE_STATE_TO_CALLBACK = 13;
    public static final int MSG_CHANGE_STATE_TO_ACTIVE = 14;
    public static final int MSG_DATA_SEND_RESULT = 15;
    public static final int MSG_TRY_TO_CALLING = 16;


    public static final int MSG_SOS_NOTIFICATION = 21;
    public static final int MSG_SOS_VOICE_CALL_TERMINATION = 22;

    public class ConstEmlLowData {
        public static final String SOS_Noti_EML_RAW_DATA = "<e-ML version=\"1.0\">\n" +
                "   <Header>\n" +
                "   <Device>\n" +
                "   </Device>\n" +
                "   <Message>\n" +
                "   <Type>Report</Type>\n" +
                "		<Service>SOS</Service>\n" +
                "		<Operation>Notification</Operation>\n" +
                "	</Message>\n" +
                "	<HULanguage>English</HULanguage>\n" +
                "	<TransmissionTimestamp>\n" +
                "		<DataTime format=\"UTC\">20150531T153000Z</DateTime>\n" +
                "	</TransmissionTimestamp>\n" +
                "	</Header>\n" +
                "	<Body>\n" +
                "		<VehicleReport>\n" +
                "			<GpsData system=\"GPS\">\n" +
                "				<Coordinate unit=\"mas\">\n" +
                "					<Lat>-136461600</Lat>\n" +
                "					<Lon>522508700</Lon>\n" +
                "				</Coordinate>\n" +
                "				<Accurate>yes</Accurate>\n" +
                "				<Velocity unit=\"kph\">100</Velocity>\n" +
                "				<Heading>90</Heading>\n" +
                "				<DataTime format=\"UTC\">20150531T152800Z</DateTime>\n" +
                "			</GpsData>\n" +
                "			<LastValidGpsData system=\"GPS\">\n" +
                "				<Coordinate unit=\"mas\">\n" +
                "					<Lat>-136461600</Lat>\n" +
                "					<Lon>522508700</Lon>\n" +
                "				</Coordinate>\n" +
                "				<Accurate>yes</Accurate>\n" +
                "				<Velocity unit=\"kph\">100</Velocity>\n" +
                "				<Heading>90</Heading>\n" +
                "				<DataTime format=\"UTC\">20150531T152800Z</DateTime>\n" +
                "			</LastValidGpsData>\n" +
                "			<EventTimestamp>\n" +
                "				<DataTime format=\"UTC\">20150531T152800Z</DateTime>\n" +
                "			</EventTimestamp>\n" +
                "			<Ignition>IG-ON</Ignition>\n" +
                "			<History system=\"GPS\" length=\"2\">\n" +
                "				<HisEntry no=\"1\">\n" +
                "					<Coordinate unit=\"mas\">\n" +
                "						<Lat></Lat>\n" +
                "						<Lon></Lon>\n" +
                "					</Coordinate>\n" +
                "					<DataTime format=\"UTC\">20150531T152800Z</DateTime>\n" +
                "				</HisEntry>\n" +
                "				<HisEntry no=\"2\">\n" +
                "					<Coordinate unit=\"mas\">\n" +
                "						<Lat></Lat>\n" +
                "						<Lon></Lon>\n" +
                "					</Coordinate>\n" +
                "					<DataTime format=\"UTC\">20150531T152800Z</DateTime>\n" +
                "				</HisEntry>\n" +
                "			</History>\n" +
                "		</VehicleReport>\n" +
                "	</Body>\n" +
                "</e-ML>\n";

        public static final String SOS_VCT_EML_RAW_DATA = "<e-ML version=\"1.0\">\n" +
                "    <Header>\n" +
                "        <Device>\n" +
                "        </Device>\n" +
                "        <Message>\n" +
                "            <Type>Request</Type>\n" +
                "            <Service>SOS</Service>\n" +
                "            <Operation>Confirm-termination</Operation>\n" +
                "        </Message>\n" +
                "        <TransmissionTimestamp>\n" +
                "            <DataTime format=\"UTC\">20150531T153000Z</DateTime>\n" +
                "        </TransmissionTimestamp>\n" +
                "    </Header>\n" +
                "    <Body>\n" +
                "        <CallTermination>customer</CallTermination>\n" +
                "    </Body>\n" +
                "</e-ML>\n";
    }
}
