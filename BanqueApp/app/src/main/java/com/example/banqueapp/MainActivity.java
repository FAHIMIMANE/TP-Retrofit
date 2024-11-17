package com.example.banqueapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;
    private List<Compte> compteList = new ArrayList<>();
    private Spinner formatSpinner;
    private Button btnAddAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view);
        formatSpinner = findViewById(R.id.format_spinner);
        btnAddAccount = findViewById(R.id.btn_add_account);

        // Configure RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        compteAdapter = new CompteAdapter(compteList, new CompteAdapter.OnCompteActionListener() {
            @Override
            public void onUpdate(Compte compte) {
                updateCompte(compte);
            }

            @Override
            public void onDelete(Compte compte) {
                deleteCompte(compte);
            }
        });

        recyclerView.setAdapter(compteAdapter);

        // Load accounts based on the selected format
        formatSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String format = parent.getItemAtPosition(position).toString();
                fetchComptes(format);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Button to add an account
        btnAddAccount.setOnClickListener(v -> addCompte());

        // Fetch comptes initially based on spinner selection
        String selectedFormat = formatSpinner.getSelectedItem() != null ? formatSpinner.getSelectedItem().toString() : "JSON";
        fetchComptes(selectedFormat);
    }

    // Fetch list of accounts based on format
    private void fetchComptes(String format) {
        // Set the Accept header value
        boolean useXml = format.equalsIgnoreCase("XML");
        String acceptHeader = useXml ? "application/xml" : "application/json";

        // Get the appropriate Retrofit instance
        CompteApi compteApi = RetrofitClient.getRetrofitInstance(acceptHeader).create(CompteApi.class);

        if (useXml) {
            // Call the XML API method
            Call<CompteList> call = compteApi.getAllComptesXml(acceptHeader);

            call.enqueue(new Callback<CompteList>() {
                @Override
                public void onResponse(Call<CompteList> call, Response<CompteList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("MainActivity", "XML data fetched successfully");
                        compteList.clear();
                        compteList.addAll(response.body().getComptes()); // Access the list inside CompteList
                        compteAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("MainActivity", "XML Response failed: " + response.errorBody());
                        Toast.makeText(MainActivity.this, "Aucun compte trouvé ou mauvais format (XML)", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CompteList> call, Throwable t) {
                    Log.e("MainActivity", "XML API call failed: " + t.getMessage(), t);
                    Toast.makeText(MainActivity.this, "Erreur réseau ou parsing incorrect (XML)", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // Call the JSON API method
            Call<List<Compte>> call = compteApi.getAllComptesJson(acceptHeader);

            call.enqueue(new Callback<List<Compte>>() {
                @Override
                public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("MainActivity", "JSON data fetched successfully");
                        compteList.clear();
                        compteList.addAll(response.body()); // Directly add the list from the response
                        compteAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("MainActivity", "JSON Response failed: " + response.errorBody());
                        Toast.makeText(MainActivity.this, "Aucun compte trouvé ou mauvais format (JSON)", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Compte>> call, Throwable t) {
                    Log.e("MainActivity", "JSON API call failed: " + t.getMessage(), t);
                    Toast.makeText(MainActivity.this, "Erreur réseau ou parsing incorrect (JSON)", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Add new account
    private void addCompte() {
        // Create AlertDialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_account, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Add New Account")
                .setPositiveButton("Add", (dialog, which) -> {
                    // Get form fields
                    EditText etBalance = dialogView.findViewById(R.id.et_balance);
                    Spinner spType = dialogView.findViewById(R.id.sp_type);

                    // Create new account
                    double balance = Double.parseDouble(etBalance.getText().toString());
                    String type = spType.getSelectedItem().toString();
                    Compte newAccount = new Compte(null, balance, "2024-11-16", type);

                    // Choose format based on spinner selection
                    String selectedFormat = formatSpinner.getSelectedItem().toString();
                   // String acceptHeader = selectedFormat.equalsIgnoreCase("XML") ? "application/xml" : "application/json";

                    // Call API to add account
                    boolean useXml = selectedFormat.equalsIgnoreCase("XML");

                    // Set Accept header based on format
                    String acceptHeader = useXml ? "application/xml" : "application/json";

                    // Call API to add account
                    CompteApi compteApi = RetrofitClient.getRetrofitInstance(acceptHeader).create(CompteApi.class);

                    compteApi.createCompte(newAccount, acceptHeader).enqueue(new Callback<Compte>() {
                        @Override
                        public void onResponse(Call<Compte> call, Response<Compte> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                compteList.add(response.body());
                                compteAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Account added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to add account", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Compte> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Update an account
    private void updateCompte(Compte compte) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_account, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Update Account")
                .setPositiveButton("Update", (dialog, which) -> {
                    // Get form fields
                    EditText etBalance = dialogView.findViewById(R.id.et_balance);
                    Spinner spType = dialogView.findViewById(R.id.sp_type);

                    // Update account data
                    compte.setSolde(Double.parseDouble(etBalance.getText().toString()));
                    compte.setType(spType.getSelectedItem().toString());

                    // Choose format based on spinner selection
                    String selectedFormat = formatSpinner.getSelectedItem().toString();
                   // String acceptHeader = selectedFormat.equalsIgnoreCase("XML") ? "application/xml" : "application/json";
                    boolean useXml = selectedFormat.equalsIgnoreCase("XML");
                    String acceptHeader = useXml ? "application/xml" : "application/json";
                    // Call API to update account
                    CompteApi compteApi = RetrofitClient.getRetrofitInstance(acceptHeader).create(CompteApi.class);
                    compteApi.updateCompte(compte.getIdLong(), compte, acceptHeader).enqueue(new Callback<Compte>() {
                        @Override
                        public void onResponse(Call<Compte> call, Response<Compte> response) {
                            if (response.isSuccessful()) {
                                fetchComptes(formatSpinner.getSelectedItem().toString());
                                Toast.makeText(MainActivity.this, "Account updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to update account", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Compte> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

        // Pre-fill fields
        EditText etBalance = dialogView.findViewById(R.id.et_balance);
        Spinner spType = dialogView.findViewById(R.id.sp_type);

        etBalance.setText(String.valueOf(compte.getSolde()));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types, android.R.layout.simple_spinner_item);
        spType.setSelection(adapter.getPosition(compte.getType()));
    }

    // Delete an account
    private void deleteCompte(Compte compte) {
        // Choose format based on spinner selection
        String selectedFormat = formatSpinner.getSelectedItem().toString();
        //String acceptHeader = selectedFormat.equalsIgnoreCase("XML") ? "application/xml" : "application/json";

        // Create Retrofit instance with the appropriate converter
        boolean useXml = selectedFormat.equalsIgnoreCase("XML");
        String acceptHeader = useXml ? "application/xml" : "application/json";
        // Call API to update account
        CompteApi compteApi = RetrofitClient.getRetrofitInstance(acceptHeader).create(CompteApi.class);

        compteApi.deleteCompte(compte.getIdLong(), acceptHeader).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    compteList.remove(compte);
                    compteAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
