using Android.Content;

namespace fs_sample
{
    public class OnMultiClickListener : Java.Lang.Object, IDialogInterfaceOnMultiChoiceClickListener
    {

        bool[] selected;

        public OnMultiClickListener(bool[] selected)
        {
            this.selected = selected;
        }

        public void OnClick(IDialogInterface dialog, int which, bool isChecked)
        {
            this.selected[which] = isChecked;
        }

    }
}