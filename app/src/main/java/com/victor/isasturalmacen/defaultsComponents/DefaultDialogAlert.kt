

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.victor.isasturalmacen.R


@Composable
fun DefaultDialogAlert(show:Boolean,
                       onDismissRequest: () -> Unit,
                       onConfirmation: () -> Unit,
                       dialogTitle: String,
                       dialogText: String){
    if(show){
        AlertDialog(onDismissRequest={onDismissRequest()},
            confirmButton = {
                TextButton(onClick = { onConfirmation() }, border = BorderStroke(1.dp, color = Color.Black)) {
                    Text(stringResource(R.string.aceptar), color = Color.Black)
                }
            },
            title = {Text(dialogTitle, color = Color.Black)},
            text = { Text(dialogText, color = Color.Black) },
            containerColor = colorResource(R.color.azul_iso),
            icon = { Icon(Icons.Default.Warning, contentDescription = "", tint = Color.Black) },
            tonalElevation = 5.dp
        )
    }
}
@Composable
@Preview
fun PreviewDialogAlert(){
    DefaultDialogAlert(show = true, onConfirmation = {}, onDismissRequest = {},
        dialogTitle = stringResource(R.string.error), dialogText = stringResource(R.string.rev_email_contr)
    )
}