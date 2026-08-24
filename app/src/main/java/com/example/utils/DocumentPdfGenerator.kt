package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.CashTransactionEntity
import com.example.data.model.LetterRequestEntity
import com.example.data.model.ResidentProfileEntity
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale

object DocumentPdfGenerator {

    /**
     * Menghasilkan dokumen PDF resmi Surat Pengantar RT/RW.
     */
    fun generateLetterPdf(
        context: Context,
        letter: LetterRequestEntity,
        resident: ResidentProfileEntity
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Ukuran A4 (poin 72 dpi)
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val textPaint = Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
        }

        val primaryColor = Color.rgb(24, 76, 120) // Deep Navy Blue
        val grayColor = Color.rgb(100, 116, 139)

        var currentY = 50f
        val leftMargin = 50f
        val rightMargin = 545f
        val contentWidth = rightMargin - leftMargin

        // 1. KOP SURAT
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textPaint.textSize = 14f
        textPaint.color = primaryColor
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("PENGURUS RUKUN TETANGGA 03 / RUKUN WARGA 02", 595f / 2, currentY, textPaint)

        currentY += 18f
        textPaint.textSize = 12f
        textPaint.color = Color.rgb(51, 65, 85)
        canvas.drawText("KELURAHAN MAJU JAYA, KECAMATAN SEJAHTERA", 595f / 2, currentY, textPaint)

        currentY += 15f
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textPaint.textSize = 9.5f
        textPaint.color = grayColor
        canvas.drawText("Sekretariat: Balai Warga RW 02 - Layanan Digital RuangWarga", 595f / 2, currentY, textPaint)

        currentY += 12f
        // Garis Pembatas Kop
        val linePaint = Paint().apply {
            color = primaryColor
            strokeWidth = 2f
        }
        canvas.drawLine(leftMargin, currentY, rightMargin, currentY, linePaint)
        linePaint.strokeWidth = 0.8f
        canvas.drawLine(leftMargin, currentY + 3, rightMargin, currentY + 3, linePaint)

        currentY += 35f

        // 2. JUDUL DOKUMEN & NOMOR
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textPaint.textSize = 13f
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(letter.jenisSurat.uppercase(Locale.getDefault()), 595f / 2, currentY, textPaint)

        currentY += 16f
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textPaint.textSize = 10f
        textPaint.color = grayColor
        canvas.drawText("Nomor: ${letter.nomorSurat}", 595f / 2, currentY, textPaint)

        currentY += 35f

        // 3. KALIMAT PEMBUKA
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.textSize = 10f
        textPaint.color = Color.BLACK
        canvas.drawText("Yang bertanda tangan di bawah ini Ketua RT 03 / RW 02 menerangkan bahwa:", leftMargin, currentY, textPaint)

        currentY += 25f

        // 4. DATA WARGA PEMOHON
        fun drawRowData(label: String, value: String) {
            textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textPaint.color = Color.rgb(71, 85, 105)
            canvas.drawText(label, leftMargin + 15, currentY, textPaint)
            canvas.drawText(":", leftMargin + 130, currentY, textPaint)
            textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textPaint.color = Color.BLACK
            canvas.drawText(value, leftMargin + 140, currentY, textPaint)
            currentY += 20f
        }

        drawRowData("Nama Lengkap", resident.nama)
        drawRowData("NIK", resident.nik)
        drawRowData("No. Kartu Keluarga", resident.noKk)
        drawRowData("Jenis Kelamin", resident.jenisKelamin)
        drawRowData("Pekerjaan", resident.pekerjaan)
        drawRowData("Alamat", resident.alamat)

        currentY += 10f

        // 5. ISI PERMOHONAN & KEPERLUAN
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textPaint.color = Color.BLACK
        canvas.drawText("Adalah benar warga kami yang berdomisili pada alamat di atas dan mengajukan", leftMargin, currentY, textPaint)
        currentY += 16f
        canvas.drawText("permohonan surat ini dengan perincian sebagai berikut:", leftMargin, currentY, textPaint)

        currentY += 22f
        drawRowData("Keperluan", letter.keperluan)
        if (letter.keteranganTambahan.isNotBlank()) {
            drawRowData("Keterangan", letter.keteranganTambahan)
        }
        drawRowData("Status Pengajuan", letter.status)
        drawRowData("Tanggal Pengajuan", letter.tanggalPengajuan)
        if (!letter.tanggalSelesai.isNullOrBlank()) {
            drawRowData("Tanggal Disetujui", letter.tanggalSelesai)
        }

        currentY += 15f
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textPaint.color = Color.BLACK
        canvas.drawText("Demikian surat pengantar ini dibuat dengan sebenarnya agar dapat dipergunakan", leftMargin, currentY, textPaint)
        currentY += 16f
        canvas.drawText("sebagaimana mestinya bagi yang bersangkutan.", leftMargin, currentY, textPaint)

        currentY += 45f

        // 6. TANDA TANGAN & VALIDASI QR MOCK
        val ttdY = currentY
        val ttdLeftX = leftMargin + 30
        val ttdRightX = 380f

        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 9.5f
        textPaint.color = Color.BLACK

        // Bagian Pemohon
        canvas.drawText("Pemohon / Warga,", ttdLeftX + 50, ttdY, textPaint)
        canvas.drawText(resident.nama, ttdLeftX + 50, ttdY + 80, textPaint)

        // Bagian Pengurus
        val tanggalToday = letter.tanggalSelesai ?: letter.tanggalPengajuan
        canvas.drawText("Dikeluarkan pada: $tanggalToday", ttdRightX + 60, ttdY - 15, textPaint)
        canvas.drawText("Ketua RT 03 / RW 02,", ttdRightX + 60, ttdY, textPaint)
        
        // Simbol Verifikasi Digital / QR Badge
        val badgePaint = Paint().apply {
            color = Color.rgb(238, 242, 255)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(ttdRightX + 15, ttdY + 12, ttdRightX + 105, ttdY + 62, 8f, 8f, badgePaint)
        
        val badgeTextPaint = Paint().apply {
            color = primaryColor
            textSize = 7.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        canvas.drawText("TERVERIFIKASI", ttdRightX + 60, ttdY + 33, badgeTextPaint)
        canvas.drawText("DIGITAL RW 02", ttdRightX + 60, ttdY + 47, badgeTextPaint)

        canvas.drawText("( Bpk. Bambang Pamungkas )", ttdRightX + 60, ttdY + 80, textPaint)

        // 7. FOOTER
        val footerPaint = Paint().apply {
            color = grayColor
            textSize = 7.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Dokumen ini digenerate secara otomatis melalui Aplikasi RuangWarga • Kode Keaslian: RW-${letter.nomorSurat.takeLast(6)}", 595f / 2, 815f, footerPaint)

        pdfDocument.finishPage(page)

        // Simpan ke direktori cache aplikasi
        return savePdfToFile(context, pdfDocument, "Surat_${letter.nomorSurat.replace("/", "_")}.pdf")
    }

    /**
     * Menghasilkan PDF Laporan Buku Kas & Keuangan RW.
     */
    fun generateCashReportPdf(
        context: Context,
        cashRecords: List<CashTransactionEntity>,
        totalSaldo: Long,
        totalMasuk: Long,
        totalKeluar: Long
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val textPaint = Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
        }

        val primaryColor = Color.rgb(24, 76, 120)
        val grayColor = Color.rgb(100, 116, 139)
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        var currentY = 50f
        val leftMargin = 40f
        val rightMargin = 555f

        // KOP
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textPaint.textSize = 14f
        textPaint.color = primaryColor
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("LAPORAN KAS & KEUANGAN RW 02", 595f / 2, currentY, textPaint)

        currentY += 18f
        textPaint.textSize = 11f
        textPaint.color = Color.rgb(51, 65, 85)
        canvas.drawText("Transparansi Kas Warga Periode Aktif", 595f / 2, currentY, textPaint)

        currentY += 15f
        val linePaint = Paint().apply {
            color = primaryColor
            strokeWidth = 2f
        }
        canvas.drawLine(leftMargin, currentY, rightMargin, currentY, linePaint)

        currentY += 30f

        // REKAP KARTU SALDO
        val cardPaint = Paint().apply {
            color = Color.rgb(241, 245, 249)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(leftMargin, currentY, rightMargin, currentY + 60, 8f, 8f, cardPaint)

        textPaint.textAlign = Paint.Align.LEFT
        textPaint.textSize = 9.5f
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textPaint.color = grayColor

        canvas.drawText("Total Pemasukan:", leftMargin + 20, currentY + 22, textPaint)
        canvas.drawText("Total Pengeluaran:", leftMargin + 180, currentY + 22, textPaint)
        canvas.drawText("Saldo Kas Tersedia:", leftMargin + 350, currentY + 22, textPaint)

        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textPaint.textSize = 11f

        textPaint.color = Color.rgb(22, 163, 74) // Green
        canvas.drawText(numberFormat.format(totalMasuk), leftMargin + 20, currentY + 45, textPaint)

        textPaint.color = Color.rgb(220, 38, 38) // Red
        canvas.drawText(numberFormat.format(totalKeluar), leftMargin + 180, currentY + 45, textPaint)

        textPaint.color = primaryColor
        canvas.drawText(numberFormat.format(totalSaldo), leftMargin + 350, currentY + 45, textPaint)

        currentY += 85f

        // TABEL TRANSAKSI
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        textPaint.textSize = 10f
        textPaint.color = primaryColor
        textPaint.textAlign = Paint.Align.LEFT

        // Header Tabel
        val tableHeaderPaint = Paint().apply {
            color = Color.rgb(226, 232, 240)
            style = Paint.Style.FILL
        }
        canvas.drawRect(leftMargin, currentY - 14, rightMargin, currentY + 8, tableHeaderPaint)

        canvas.drawText("Tanggal", leftMargin + 8, currentY, textPaint)
        canvas.drawText("Kategori / Keterangan", leftMargin + 90, currentY, textPaint)
        canvas.drawText("Tipe", leftMargin + 320, currentY, textPaint)
        canvas.drawText("Nominal", leftMargin + 400, currentY, textPaint)

        currentY += 20f

        // Data Baris (Maksimal 18 data pertama di 1 halaman)
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textPaint.textSize = 8.5f

        for (item in cashRecords.take(18)) {
            val isIncome = item.tipe.lowercase().contains("masuk")
            textPaint.color = Color.rgb(51, 65, 85)

            canvas.drawText(item.tanggal, leftMargin + 8, currentY, textPaint)
            
            val deskripsiPendek = if (item.keterangan.length > 35) item.keterangan.take(32) + "..." else item.keterangan
            canvas.drawText("${item.kategori} - $deskripsiPendek", leftMargin + 90, currentY, textPaint)

            canvas.drawText(if (isIncome) "Masuk" else "Keluar", leftMargin + 320, currentY, textPaint)

            textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textPaint.color = if (isIncome) Color.rgb(22, 163, 74) else Color.rgb(220, 38, 38)
            val nominalStr = (if (isIncome) "+ " else "- ") + numberFormat.format(item.jumlah)
            canvas.drawText(nominalStr, leftMargin + 400, currentY, textPaint)

            textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            currentY += 18f
        }

        // Tanda Tangan Bendahara & Ketua RW
        currentY = 720f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 9f
        textPaint.color = Color.BLACK

        canvas.drawText("Mengetahui,", leftMargin + 90, currentY, textPaint)
        canvas.drawText("Ketua RW 02", leftMargin + 90, currentY + 14, textPaint)
        canvas.drawText("( Bpk. Hendra Gunawan )", leftMargin + 90, currentY + 65, textPaint)

        canvas.drawText("Dibuat oleh,", rightMargin - 90, currentY, textPaint)
        canvas.drawText("Bendahara RW 02", rightMargin - 90, currentY + 14, textPaint)
        canvas.drawText("( Ibu Siti Rahmawati )", rightMargin - 90, currentY + 65, textPaint)

        // Footer
        val footerPaint = Paint().apply {
            color = grayColor
            textSize = 7.5f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Laporan ini sah dan diterbitkan melalui Sistem Digital RuangWarga", 595f / 2, 815f, footerPaint)

        pdfDocument.finishPage(page)

        return savePdfToFile(context, pdfDocument, "Laporan_Kas_RW_${System.currentTimeMillis()}.pdf")
    }

    private fun savePdfToFile(context: Context, pdfDocument: PdfDocument, fileName: String): File? {
        return try {
            val docsDir = File(context.cacheDir, "documents")
            if (!docsDir.exists()) {
                docsDir.mkdirs()
            }
            val file = File(docsDir, fileName)
            val fos = FileOutputStream(file)
            pdfDocument.writeTo(fos)
            fos.flush()
            fos.close()
            pdfDocument.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            null
        }
    }

    /**
     * Membuka atau membagikan file PDF menggunakan Android Intent Chooser.
     */
    fun openOrSharePdf(context: Context, pdfFile: File, title: String = "Dokumen RuangWarga") {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, title)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(intent, "Buka / Bagikan $title")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal membagikan file PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
