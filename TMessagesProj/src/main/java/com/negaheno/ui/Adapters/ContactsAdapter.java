/*
 * This is the source code of Telegram for Android v. 1.3.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.negaheno.ui.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.negaheno.android.AndroidUtilities;
import com.negaheno.android.LocaleController;
import com.negaheno.android.MessagesController;
import com.negaheno.messenger.TLRPC;
import com.negaheno.ui.Cells.LetterSectionCell;
import com.negaheno.ui.Cells.UserCell;
import com.negaheno.android.ContactsController;
import com.negaheno.messenger.R;
import com.negaheno.ui.AnimationCompat.ViewProxy;
import com.negaheno.ui.Cells.DividerCell;
import com.negaheno.ui.Cells.GreySectionCell;
import com.negaheno.ui.Cells.TextCell;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsAdapter extends BaseSectionsAdapter {

    private Context mContext;
    private boolean onlyUsers;
    private boolean needPhonebook;
    private HashMap<Integer, TLRPC.User> ignoreUsers;
    private HashMap<Integer, ?> checkedMap;
    private boolean scrolling;

    public ContactsAdapter(Context context, boolean arg1, boolean arg2, HashMap<Integer, TLRPC.User> arg3) {
        mContext = context;
        onlyUsers = arg1;
        needPhonebook = arg2;
        ignoreUsers = arg3;
    }

    public void setCheckedMap(HashMap<Integer, ?> map) {
        checkedMap = map;
    }

    public void setIsScrolling(boolean value) {
        scrolling = value;
    }

    @Override
    public Object getItem(int section, int position) {
        if (onlyUsers) {
            if (section < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section));
                if (position < arr.size()) {
                    return MessagesController.getInstance().getUser(arr.get(position).user_id);
                }
            }
            return null;
        } else {
            if (section == 0) {
                return null;
            } else {
                if (section - 1 < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                    ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section - 1));
                    if (position < arr.size()) {
                        return MessagesController.getInstance().getUser(arr.get(position).user_id);
                    }
                    return null;
                }
            }
        }
        if (needPhonebook) {
            return ContactsController.getInstance().phoneBookContacts.get(position);
        }
        return null;
    }

    @Override
    public boolean isRowEnabled(int section, int row) {
        if (onlyUsers) {
            ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section));
            return row < arr.size();
        } else {
            if (section == 0) {
                if (needPhonebook) {
                    if (row == 1) {
                        return false;
                    }
                } else {
                    if (row == 3) {
                        return false;
                    }
                }
                return true;
            } else if (section - 1 < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section - 1));
                return row < arr.size();
            }
        }
        return true;
    }

    @Override
    public int getSectionCount() {
        int count = ContactsController.getInstance().sortedUsersSectionsArray.size();
        if (!onlyUsers) {
            count++;
        }
        if (needPhonebook) {
            count++;
        }
        return count;
    }

    @Override
    public int getCountForSection(int section) {
        if (onlyUsers) {
            if (section < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section));
                int count = arr.size();
                if (section != (ContactsController.getInstance().sortedUsersSectionsArray.size() - 1) || needPhonebook) {
                    count++;
                }
                return count;
            }
        } else {
            if (section == 0) {
                if (needPhonebook) {
                    return 2;
                } else {
                    return 4;
                }
            } else if (section - 1 < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section - 1));
                int count = arr.size();
                if (section - 1 != (ContactsController.getInstance().sortedUsersSectionsArray.size() - 1) || needPhonebook) {
                    count++;
                }
                return count;
            }
        }
        if (needPhonebook) {
            return ContactsController.getInstance().phoneBookContacts.size();
        }
        return 0;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new LetterSectionCell(mContext);
        }
        if (onlyUsers) {
            if (section < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ((LetterSectionCell) convertView).setLetter(ContactsController.getInstance().sortedUsersSectionsArray.get(section));
            } else {
                ((LetterSectionCell) convertView).setLetter("");
            }
        } else {
            if (section == 0) {
                ((LetterSectionCell) convertView).setLetter("");
            } else if (section - 1 < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ((LetterSectionCell) convertView).setLetter(ContactsController.getInstance().sortedUsersSectionsArray.get(section - 1));
            } else {
                ((LetterSectionCell) convertView).setLetter("");
            }
        }
        return convertView;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(section, position);
        if (type == 4) {
            if (convertView == null) {
                convertView = new DividerCell(mContext);
                convertView.setPadding(AndroidUtilities.dp(LocaleController.isRTL ? 28 : 72), 0, AndroidUtilities.dp(LocaleController.isRTL ? 72 : 28), 0);
            }
        } else if (type == 3) {
            if (convertView == null) {
                convertView = new GreySectionCell(mContext);
                ((GreySectionCell) convertView).setText(LocaleController.getString("Contacts", R.string.Contacts).toUpperCase());
                //((GreySectionCell) convertView).setText(String.format(Locale.US, " %d " + LocaleController.getString("Contacts", R.string.Contacts).toUpperCase(), arr0.size()));
                ((GreySectionCell) convertView).setBackgroundColor(AndroidUtilities.getIntDef("contactsRowColor", 0xffffffff));
            }
        } else if (type == 2) {
            if (convertView == null) {
                convertView = new TextCell(mContext);
            }
            TextCell actionCell = (TextCell) convertView;
            actionCell.setTextColor(AndroidUtilities.getIntDef("contactsNameColor", 0xff000000));
            if (needPhonebook) {
                actionCell.setTextAndIcon(LocaleController.getString("InviteFriends", R.string.InviteFriends), R.drawable.menu_invite);
            } else {
                if (position == 0) {
                    actionCell.setTextAndIcon(LocaleController.getString("NewGroup", R.string.NewGroup), R.drawable.menu_newgroup);
                } else if (position == 1) {
                    actionCell.setTextAndIcon(LocaleController.getString("NewSecretChat", R.string.NewSecretChat), R.drawable.menu_secret);
                } else if (position == 2) {
                    actionCell.setTextAndIcon(LocaleController.getString("NewBroadcastList", R.string.NewBroadcastList), R.drawable.menu_broadcast);
                }
            }
        } else if (type == 1) {
            if (convertView == null) {
                convertView = new TextCell(mContext);
                ((TextCell) convertView).setTextColor(AndroidUtilities.getIntDef("contactsNameColor", 0xff000000));
                ((TextCell) convertView).setTextSize(AndroidUtilities.getIntDef("contactsNameSize", 16));
            }
            ContactsController.Contact contact = ContactsController.getInstance().phoneBookContacts.get(position);
            if (contact.first_name != null && contact.last_name != null) {
                ((TextCell) convertView).setText(contact.first_name + " " + contact.last_name);
            } else if (contact.first_name != null && contact.last_name == null) {
                ((TextCell) convertView).setText(contact.first_name);
            } else {
                ((TextCell) convertView).setText(contact.last_name);
            }
        } else if (type == 0) {
            if (convertView == null) {
                convertView = new UserCell(mContext, 58);
                //((UserCell) convertView).setStatusColors(0xffa8a8a8, 0xff3b84c0);
                ((UserCell) convertView).setStatusColors(AndroidUtilities.getIntDef("contactsStatusColor", 0xffa8a8a8), AndroidUtilities.getIntDef("contactsOnlineColor", AndroidUtilities.getIntDarkerColor("themeColor",0x15)));
                ((UserCell) convertView).setNameColor(AndroidUtilities.getIntDef("contactsNameColor", 0xff000000));
            }

            ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section - (onlyUsers ? 0 : 1)));
            TLRPC.User user = MessagesController.getInstance().getUser(arr.get(position).user_id);
            ((UserCell)convertView).setData(user, null, null, 0);
            if (checkedMap != null) {
                ((UserCell) convertView).setChecked(checkedMap.containsKey(user.id), !scrolling  && Build.VERSION.SDK_INT > 10);
            }
            if (ignoreUsers != null) {
                if (ignoreUsers.containsKey(user.id)) {
                    ViewProxy.setAlpha(convertView, 0.5f);
                } else {
                    ViewProxy.setAlpha(convertView, 1.0f);
                }
            }
        }
        parent.setBackgroundColor(AndroidUtilities.getIntDef("contactsRowColor", 0xffffffff));
        return convertView;
    }

    @Override
    public int getItemViewType(int section, int position) {
        if (onlyUsers) {
            ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section));
            return position < arr.size() ? 0 : 4;
        } else {
            if (section == 0) {
                if (needPhonebook) {
                    if (position == 1) {
                        return 3;
                    }
                } else {
                    if (position == 3) {
                        return 3;
                    }
                }
                return 2;
            } else if (section - 1 < ContactsController.getInstance().sortedUsersSectionsArray.size()) {
                ArrayList<TLRPC.TL_contact> arr = ContactsController.getInstance().usersSectionsDict.get(ContactsController.getInstance().sortedUsersSectionsArray.get(section - 1));
                return position < arr.size() ? 0 : 4;
            }
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }
}
