package com.zhisland.android.blog.message.view;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.mvp.view.IMvpListView;

import org.jivesoftware.smackx.packet.MUCUser;

/**
 * Created by arthur on 2016/9/13.
 */
public interface IFansMessageView extends IMvpListView<InviteUser> {

    void updateItem(InviteUser inviteUser);
}
