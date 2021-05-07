package dao;

import model.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDao {
	private List<Usuario> usuarios;
	private int maxId = 0;

	private File file;
	private FileOutputStream fos;
	private ObjectOutputStream outputFile;

	public int getMaxId() {
		return maxId;
	}

	public UsuarioDao(String filename) throws IOException {
		file = new File(filename);
		usuarios = new ArrayList<Usuario>();
		if (file.exists()) {
			readFromFile();
		}

	}

	public void add(Usuario usuario) {
		try {
			usuarios.add(usuario);
			this.maxId = (usuario.getId() > this.maxId) ? usuario.getId() : this.maxId;
			this.saveToFile();
		} catch (Exception e) {
		}
	}

	public Usuario get(int id) {
		for (Usuario usuario : usuarios) {
			if (id == usuario.getId()) {
				return usuario;
			}
		}
		return null;
	}

	public void update(Usuario p) {
		int index = usuarios.indexOf(p);
		if (index != -1) {
			usuarios.set(index, p);
			this.saveToFile();
		}
	}

	public void remove(Usuario p) {
		int index = usuarios.indexOf(p);
		if (index != -1) {
			usuarios.remove(index);
			this.saveToFile();
		}
	}

	public List<Usuario> getAll() {
		return usuarios;
	}

	private List<Usuario> readFromFile() {
		usuarios.clear();
		Usuario usuario = null;
		try (FileInputStream fis = new FileInputStream(file);
				ObjectInputStream inputFile = new ObjectInputStream(fis)) {

			while (fis.available() > 0) {
				usuario = (Usuario) inputFile.readObject();
				usuarios.add(usuario);
				maxId = (usuario.getId() > maxId) ? usuario.getId() : maxId;
			}
		} catch (Exception e) {
			System.out.println("ERRO ao gravar usuario no disco!");
			e.printStackTrace();
		}
		return usuarios;
	}

	private void saveToFile() {
		try {
			fos = new FileOutputStream(file, false);
			outputFile = new ObjectOutputStream(fos);

			for (Usuario usuario : usuarios) {
				outputFile.writeObject(usuario);
			}
			outputFile.flush();
			this.close();
		} catch (Exception e) {
			System.out.println("ERRO ao gravar usuario no disco!");
			e.printStackTrace();
		}
	}

	private void close() throws IOException {
		outputFile.close();
		fos.close();
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.saveToFile();
			this.close();
		} catch (Exception e) {
			System.out.println("ERRO ao salvar a base de dados no disco!");
			e.printStackTrace();
		}
	}
}