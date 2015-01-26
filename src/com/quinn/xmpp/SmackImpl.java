package com.quinn.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.packet.VCard;


public class SmackImpl implements Smack {

	private static volatile SmackImpl INSTANCE = null;

	private String server_mode = "";
	private String username;
	private String password;
	private boolean isConnect;

	private HashMap<String, ArrayList<FileTransferRequest>> requestMap = new HashMap<String, ArrayList<FileTransferRequest>>();
	private static final HashMap<String, String> jid_resource_map = new HashMap<String, String>();

	private SmackImpl() {

	}
	public void setServerMode(String mode){
		this.server_mode = mode;
	}
	public String getServerMode(){
		return this.server_mode;
	}
	// thread safe and performance promote
	public static SmackImpl getInstance() {
		if (INSTANCE == null) {
			synchronized (SmackImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (INSTANCE == null) {
					INSTANCE = new SmackImpl();
				}
			}
		}
		return INSTANCE;
	}

	public void connect(String server, int port, String service) {

		ConnectionManager.connect(server, port,service);
		XMPPConnection conn = ConnectionManager.getConnection();
		if (conn.isConnected()) {
			
		} else {
			
		}
	}

//	/**
//	 * method of login
//	 * 
//	 * @param username
//	 *            :usernmae of the user
//	 * @param password
//	 *            :password of the user
//	 * @return if login successfully
//	 */
//	@Override
//	public int login(String username, String password) {
//		// TODO Auto-generated method stub
//		XMPPConnection conn = ConnectionManager.getConnection();
//		if (conn == null || conn.isConnected() == false) {
//			return Constants.LOGIN_CONNECT_FAIL;
//		}
//		this.username = username;
//		this.password = password;
//		try {
//			conn.login(username, password);
//			L.i("after login: " + conn.isAuthenticated());
//			return Constants.LOGIN_SUCCESS;
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			return Constants.LOGIN_USERNAME_PSW_ERROR;
//		}
//
//	}

	@Override
	public void turnOnlineToAll() {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		Presence presence = new Presence(Presence.Type.available);
		conn.sendPacket(presence);
	}

	@Override
	public void turnOnlineToSomeone(String username) {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		Presence presence = new Presence(Presence.Type.available);
		presence.setTo(username);
		conn.sendPacket(presence);
	}

	@Override
	public void turnDownlineToAll() {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		Presence presence = new Presence(Presence.Type.unavailable);
		conn.sendPacket(presence);
	}

	@Override
	public void turnDownlineToSomeone(String username) {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		Presence presence = new Presence(Presence.Type.unavailable);
		presence.setTo(username);
		conn.sendPacket(presence);
	}

	@Override
	public Collection<RosterEntry> getAllFriend() {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		Collection<RosterEntry> collection;
		collection = conn.getRoster().getEntries();
		return collection;
	}



	@Override
	public ArrayList<RosterGroup> getGroups() {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		Roster roster = conn.getRoster();
		ArrayList<RosterGroup> groupList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> groupCollect = roster.getGroups();

		Iterator<RosterGroup> groupIter = groupCollect.iterator();
		while (groupIter.hasNext()) {

			groupList.add(groupIter.next());
		}
		return groupList;
	}

	@Override
	public String isOnline(String jid) {
		boolean online = false;
		Roster roster = ConnectionManager.getConnection().getRoster();
		Presence p6e = roster.getPresence(jid);

		if (p6e.getType().equals(Presence.Type.available))
			online = true;
		else
			online = false;

		return "";

	}

	@Override
	public String getNickname(String jid) {
		String nickname = jid;
//		if(this.getServerMode().equals(Constants.MODE_GTALK)){
//			return getEntry(jid).getName();
//		}
		VCard vcard = new VCard();
		
		try {

			vcard.load(ConnectionManager.getConnection(), jid);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nickname = vcard.getNickName();
		// somebody have set a nickname,somebody have not
//		if (nickname == null) {
//			nickname = ValueUtil.getItemName(jid);
//		}
		return nickname;
	}

//	@Override
//	public ArrayList<RowContacts> getContactsRows() {
//		// TODO Auto-generated method stub
//		XMPPConnection conn = ConnectionManager.getConnection();
//		String jid = conn.getUser();
//		//L.i("jID:" + jid);
//		ArrayList<RowContacts> rows = new ArrayList<RowContacts>();
//
//		Roster roster = conn.getRoster();
//		Iterator<RosterGroup> iter = roster.getGroups().iterator();
//		// go through each group
//		while (iter.hasNext()) {
//			RosterGroup rG = iter.next();
//			String group = rG.getName();
//			String friend_jID = null;
//			String nickname = null;
//			String online = null;
//			String photo = null;
//			String signature = null;
//			Collection<RosterEntry> cE = rG.getEntries();
//			int groupSize = cE.size();
//			Iterator<RosterEntry> iR_iter = cE.iterator();
//			// go through each friend
//			int intoFlag = 0;
//			while (iR_iter.hasNext()) {
//				intoFlag = 1;
//				RosterEntry rE = iR_iter.next();
//				friend_jID = rE.getUser();
//				nickname = getNickname(rE.getUser());
//				online = isOnline(rE.getUser());
//			
//				photo = null;
//				signature = null;
//				RowContacts row = new RowContacts(jid, group, friend_jID,
//						nickname, online, photo, signature);
//				rows.add(row);
//			}
//			if (intoFlag == 0) {
//				RowContacts row = new RowContacts(jid, group, null, null, null,
//						null, null);
//				rows.add(row);
//			}
//
//		}
//		//Test.outputContactsRows(rows);
//		return rows;
//	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		if (conn != null) {
			conn.disconnect();
		}
	}

	@Override
	public void addConnectionListener(ConnectionListener cListener) {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		conn.addConnectionListener(cListener);
	}

	@Override
	public XMPPConnection getConnection() {
		// TODO Auto-generated method stub
		return ConnectionManager.getConnection();
	}

	@Override
	public void acceptFriend(String requestJID, String groupName) {
		// TODO Auto-generated method stub
		Presence newp = new Presence(Presence.Type.subscribed);
		newp.setMode(Presence.Mode.available);
		newp.setPriority(24);
		String nickname = getNickname(requestJID);
		newp.setTo(requestJID);
		XMPPConnection conn = ConnectionManager.getConnection();

		Presence subscription = new Presence(Presence.Type.subscribe);
		subscription.setTo(requestJID);
		conn.sendPacket(subscription);
		addEntry(requestJID, groupName);

	}

	@Override
	public void addGroup(String groupname) {
		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		conn.getRoster().createGroup(groupname);
	}

	@Override
	public void addEntry(String jid, String groupname) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		XMPPConnection conn = ConnectionManager.getConnection();
		String nickname = getNickname(jid);
		try {
			conn.getRoster().createEntry(jid, nickname,
					new String[] { groupname });
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void subscribed(String jid) {
		// TODO Auto-generated method stub
		Presence newp = new Presence(Presence.Type.subscribed);
		newp.setMode(Presence.Mode.available);
		newp.setPriority(24);
		newp.setTo(jid);
		XMPPConnection conn = ConnectionManager.getConnection();
		conn.sendPacket(newp);
	}

	@Override
	public void subscribe(String jid) {
		// TODO Auto-generated method stub
		Presence newp = new Presence(Presence.Type.subscribe);
		newp.setMode(Presence.Mode.available);
		newp.setPriority(24);
		newp.setTo(jid);
		XMPPConnection conn = ConnectionManager.getConnection();
		conn.sendPacket(newp);
		// addEntry(jid,"friend");
	}

	@Override
	public void unSubscribe(String jid) {
		// TODO Auto-generated method stub
		Presence newp = new Presence(Presence.Type.unsubscribe);
		newp.setMode(Presence.Mode.available);
		newp.setPriority(24);
		newp.setTo(jid);
		XMPPConnection conn = ConnectionManager.getConnection();
		conn.sendPacket(newp);
	}

	@Override
	public void unSubscribed(String jid) {
		// TODO Auto-generated method stub
		Presence newp = new Presence(Presence.Type.unsubscribed);
		newp.setMode(Presence.Mode.available);
		newp.setPriority(24);
		newp.setTo(jid);
		XMPPConnection conn = ConnectionManager.getConnection();
		conn.sendPacket(newp);
	}

	@Override
	public String getJID() {
		// TODO Auto-generated method stub
		String string = this.getConnection().getUser();
		return "";
//		return ValueUtil.deleteSth(string, Constants.DELETE_STH);
	}

	@Override
	public void removeEntry(String jid) {
		RosterEntry entry = this.getConnection().getRoster().getEntry(jid);
		try {
			this.getConnection().getRoster().removeEntry(entry);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public RosterEntry getEntry(String u_jid) {
		// TODO Auto-generated method stub
		return this.getConnection().getRoster().getEntry(u_jid);

	}

	@Override
	public void markResource(String fullyJID) {
		// TODO Auto-generated method stub
		String bareJID = fullyJID.substring(0, fullyJID.lastIndexOf("/"));
		this.jid_resource_map.put(bareJID, fullyJID);
	}

	@Override
	public String getFullyJID(String bareJID) {

		return jid_resource_map.get(bareJID) == null ? bareJID
				: jid_resource_map.get(bareJID);
	}

	@Override
	public FileTransferRequest getRequestList(String u_jid, String filename) {
		// TODO Auto-generated method stub

		ArrayList<FileTransferRequest> list = requestMap.get(u_jid);
		//Test.outputRequestMap(requestMap);
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getFileName().equals(filename))
				return list.get(i);
		}
		return null;

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}



	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}
	
	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub
		this.username = username;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password = password;
	}
	@Override
	public boolean isConnect() {
		return isConnect;
	}
	@Override
	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	@Override
	public String regist(String account, String password) {
		// TODO Auto-generated method stub
		if (getConnection() == null)  
            return "0";  
        Registration reg = new Registration();  
        reg.setType(IQ.Type.SET);  
        reg.setTo(getConnection().getServiceName());  
        // ע������createAccountע��ʱ��������UserName������jid����"@"ǰ��Ĳ��֡�  
        reg.setUsername(account);  
        reg.setPassword(password);  
        // ���addAttribute����Ϊ�գ������������������־��android�ֻ������İɣ���������  
        reg.addAttribute("android", "geolo_createUser_android");  
        PacketFilter filter = new AndFilter(new PacketIDFilter(  
                reg.getPacketID()), new PacketTypeFilter(IQ.class));  
        PacketCollector collector = getConnection().createPacketCollector(  
                filter);  
        getConnection().sendPacket(reg);  
        IQ result = (IQ) collector.nextResult(SmackConfiguration  
                .getPacketReplyTimeout());  
        // Stop queuing resultsֹͣ����results���Ƿ�ɹ��Ľ����  
        collector.cancel();  
        if (result == null) {  
        	//T.mToast(context, content)
           // Log.e("regist", "No response from server.");  
            return "0";  
        } else if (result.getType() == IQ.Type.RESULT) {  
           // Log.v("regist", "regist success.");  
            return "1";  
        } else { // if (result.getType() == IQ.Type.ERROR)  
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {  
               // Log.e("regist", "IQ.Type.ERROR: "  
                       // + result.getError().toString());  
                return "2";  
            } else {  
                //Log.e("regist", "IQ.Type.ERROR: "  
                return "3";  
            }  
        }  
	}
	@Override
	public int login(String username, String password) {
		// TODO Auto-generated method stub
		return 0;
	}
}
